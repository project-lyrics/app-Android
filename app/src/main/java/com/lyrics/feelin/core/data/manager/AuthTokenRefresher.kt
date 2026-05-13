package com.lyrics.feelin.core.data.manager

import com.lyrics.feelin.core.data.datasource.remote.AuthRemoteDataSource
import com.lyrics.feelin.core.domain.model.AuthToken
import com.lyrics.feelin.util.toServerErrorDto
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException

class AuthTokenRefreshException(
    val errorCode: String?,
    cause: Throwable,
) : Exception(cause)

/**
 * 서버 JWT 재발급 공통 경로입니다.
 *
 * 이 클래스는 런타임 401 재인증에서도 사용되므로
 * 일시적인 네트워크/서버 실패만으로는 저장된 refresh token을 삭제하지 않습니다.
 * 토큰 만료·무효처럼 서버가 세션 종료를 확정한 인증 에러 코드에만 토큰을 정리하고,
 * 앱 시작 자동 로그인의 더 강한 실패 정책은 [AuthRepository.restoreSession]에서 조합합니다.
 */
@Singleton
class AuthTokenRefresher @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authManager: AuthManager
) {
    private val refreshMutex = Mutex()

    suspend fun refreshServerToken(staleAccessToken: String? = null): Result<AuthToken> {
        return refreshMutex.withLock {
            authManager.initializationComplete.await()

            val cachedAccessToken = authManager.accessToken.value
            val cachedRefreshToken = authManager.refreshToken.value
            val cachedUserId = authManager.userId.value

            val cachedAuthToken = createCachedAuthToken(
                staleAccessToken = staleAccessToken,
                cachedAccessToken = cachedAccessToken,
                cachedRefreshToken = cachedRefreshToken,
                cachedUserId = cachedUserId,
            )
            if (cachedAuthToken != null) {
                return@withLock Result.success(
                    cachedAuthToken
                )
            }

            val refreshToken = cachedRefreshToken
                ?: return@withLock clearTokensAndFail(IllegalStateException("Refresh token is null"))

            authRemoteDataSource.reIssueToken(refreshToken = refreshToken)
                .fold(
                    onSuccess = { authToken ->
                        authManager.saveServerToken(
                            accessToken = authToken.accessToken,
                            refreshToken = authToken.refreshToken,
                            userId = authToken.userId,
                        )
                        Result.success(authToken)
                    },
                    onFailure = { exception ->
                        if (exception is CancellationException) {
                            throw exception
                        }
                        handleRefreshFailure(exception)
                    },
                )
        }
    }

    private suspend fun handleRefreshFailure(exception: Throwable): Result<AuthToken> {
        val refreshException = exception.toAuthTokenRefreshException()

        // 401 재인증 경로에서는 일시 실패 후 다음 요청에서 다시 복구할 수 있도록 토큰을 보존합니다.
        return if (refreshException.errorCode in TERMINAL_AUTH_ERROR_CODES) {
            clearTokensAndFail(refreshException)
        } else {
            Result.failure(refreshException)
        }
    }

    private suspend fun clearTokensAndFail(exception: Throwable): Result<AuthToken> {
        authManager.clearTokens()
        return Result.failure(exception)
    }

    private fun Throwable.toAuthTokenRefreshException(): AuthTokenRefreshException {
        val errorCode = if (this is HttpException) {
            toServerErrorDto().errorCode
        } else {
            null
        }

        return AuthTokenRefreshException(
            errorCode = errorCode,
            cause = this,
        )
    }

    private fun createCachedAuthToken(
        staleAccessToken: String?,
        cachedAccessToken: String?,
        cachedRefreshToken: String?,
        cachedUserId: Long?
    ): AuthToken? {
        val staleToken = staleAccessToken ?: return null
        val accessToken = cachedAccessToken ?: return null
        val refreshToken = cachedRefreshToken ?: return null
        val userId = cachedUserId ?: return null

        if (accessToken == staleToken) {
            return null
        }

        return AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId,
        )
    }

    companion object {
        private val TERMINAL_AUTH_ERROR_CODES = setOf(
            "01001",
            "01002",
            "01004",
            "01008",
        )
    }
}

package com.lyrics.feelin.core.data.repository

import android.util.Log
import com.lyrics.feelin.core.data.datasource.remote.AuthRemoteDataSource
import com.lyrics.feelin.core.data.datasource.remote.dto.exception.FeelinServerException
import com.lyrics.feelin.core.data.datasource.sdk.GoogleAuthDataSource
import com.lyrics.feelin.core.data.datasource.sdk.KakaoAuthDataSource
import com.lyrics.feelin.core.data.manager.AuthManager
import com.lyrics.feelin.core.data.manager.AuthTokenRefreshException
import com.lyrics.feelin.core.data.manager.AuthTokenRefresher
import com.lyrics.feelin.core.domain.model.AuthToken
import com.lyrics.feelin.core.domain.model.OAuthProvider
import com.lyrics.feelin.core.domain.model.OAuthToken
import com.lyrics.feelin.core.domain.model.SignUpData
import com.lyrics.feelin.util.toServerErrorDto
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

private const val UNKNOWN_SERVER_ERROR_CODE = "-1"
private const val TAG = "AuthRepository"

sealed interface RestoreSessionResult {
    data object Authenticated : RestoreSessionResult
    data object Unauthenticated : RestoreSessionResult
    data class Failed(val errorCode: String) : RestoreSessionResult
}

/**
 * 인증 비즈니스 로직을 담당하는 Repository
 *
 * **책임:**
 * - OAuth SDK 호출 (Kakao, Google)
 * - Backend API 호출 (토큰 발급)
 * - AuthManager를 통한 토큰 저장/삭제
 *
 * **멀티모듈 전환 시:**
 * - :core:data 모듈로 이동
 * - Domain Layer는 이 Repository만 의존
 */
@Singleton
class AuthRepository @Inject constructor(
    private val kakaoAuthDataSource: KakaoAuthDataSource,
    @Suppress("UnusedPrivateMember") // TODO(@이대근): 구글 로그인 구현 중 어노테이션 제거할 것. 2025.10.02.
    private val googleAuthDataSource: GoogleAuthDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authManager: AuthManager,
    private val authTokenRefresher: AuthTokenRefresher
) {
    // ========== 로그인 상태 노출 ==========

    val isLoggedIn: StateFlow<Boolean> = authManager.isLoggedIn

    val userId: StateFlow<Long?> = authManager.userId

    // ========== 자동 로그인 ==========

    @Suppress("TooGenericExceptionCaught")
    suspend fun restoreSession(): RestoreSessionResult {
        authManager.initializationComplete.await()

        return try {
            when {
                !authManager.hasRefreshToken() -> {
                    RestoreSessionResult.Unauthenticated
                }

                !authManager.hasValidAccessToken() -> {
                    restoreSessionWithRefresh()
                }

                authRemoteDataSource.validateToken().getOrNull()?.status == true -> {
                    RestoreSessionResult.Authenticated
                }

                else -> {
                    restoreSessionWithRefresh()
                }
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            runCatching {
                authManager.clearTokens()
            }
            RestoreSessionResult.Failed(errorCode = exception.toServerErrorCode())
        }
    }

    /**
     * 앱 시작 자동 로그인은 복구 실패를 세션 종료로 취급합니다.
     *
     * 런타임 401 재인증은 일시 실패 시 토큰을 보존하지만,
     * 앱 시작 단계에서는 사용자에게 메인 화면을 보여주기 전에 세션 사용 가능 여부를 확정해야 합니다.
     * 따라서 refresh 실패 원인이 네트워크 오류인지 서버 인증 오류인지와 무관하게
     * 로컬 토큰을 비우고 로그인 화면으로 보냅니다.
     */
    private suspend fun restoreSessionWithRefresh(): RestoreSessionResult {
        return authTokenRefresher.refreshServerToken()
            .fold(
                onSuccess = { RestoreSessionResult.Authenticated },
                onFailure = { exception ->
                    if (exception is CancellationException) {
                        throw exception
                    }
                    authManager.clearTokens()
                    RestoreSessionResult.Failed(errorCode = exception.toServerErrorCode())
                },
            )
    }

    // ========== 로그인 ==========

    /**
     * 백엔드 서버 로그인을 수행하고 JWT 토큰을 발급받습니다.
     *
     * HTTP_NOT_FOUND(404): 회원가입이 필요한 경우 OAuth 토큰을 임시 저장하고 예외 발생
     */
    suspend fun authenticateWithBackend(
        provider: OAuthProvider,
        oauthToken: OAuthToken
    ): Result<Unit> {
        val result = authRemoteDataSource.signIn(
            provider = provider,
            oAuthToken = oauthToken
        )

        val failure = result.exceptionOrNull()?.let { error ->
            mapAuthenticationFailure(
                error = error,
                provider = provider,
                oauthToken = oauthToken,
            )
        }

        if (failure != null) {
            return Result.failure(exception = failure)
        }

        val response = result.getOrThrow()

        val authToken = AuthToken(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            userId = response.userId
        )

        // 토큰 저장
        saveAllTokens(authToken, oauthToken, provider)

        return Result.success(Unit)
    }

    /** 자체 서버 로그인에서 예외가 발생할 때 알맞는 예외를 할당하고 그 이전에 필요한 동작을 진행합니다. */
    private suspend fun mapAuthenticationFailure(
        error: Throwable,
        provider: OAuthProvider,
        oauthToken: OAuthToken
    ): Throwable {
        if (error !is HttpException) {
            return error
        }

        return when (error.code()) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val errorDto = error.toServerErrorDto()
                FeelinServerException(description = errorDto)
            }

            HttpURLConnection.HTTP_NOT_FOUND -> {
                handleRegistrationRequired(provider, oauthToken)
                val errorDto = error.toServerErrorDto()
                FeelinServerException(description = errorDto)
            }

            else -> error
        }
    }

    /** 자체 로그인 도중 회원가입이 필요한 경우 OAuth 토큰 임시 저장 */
    private suspend fun handleRegistrationRequired(
        provider: OAuthProvider,
        oauthToken: OAuthToken
    ) {
        authManager.updateOAuthToken(
            oauthAccessToken = oauthToken.accessToken,
            oauthRefreshToken = oauthToken.refreshToken,
        )
        authManager.updateOAuthProvider(provider = provider)
    }

    /** 모든 토큰 저장 (Backend JWT + OAuth 정보) */
    private suspend fun saveAllTokens(
        authToken: AuthToken,
        oauthToken: OAuthToken,
        provider: OAuthProvider
    ) {
        authManager.saveAllToken(
            accessToken = authToken.accessToken,
            refreshToken = authToken.refreshToken,
            userId = authToken.userId,
            oauthProvider = provider,
            oauthAccessToken = oauthToken.accessToken,
            oauthRefreshToken = oauthToken.refreshToken
        )
    }

    // ========== 로그아웃 ==========

    /**
     * 로그아웃
     *
     * **플로우:**
     * 1. Backend에 로그아웃 요청
     * 2. SDK 로그아웃 (Kakao/Google)
     * 3. 성공/실패 여부와 무관하게 AuthManager에서 토큰 삭제
     */
    suspend fun logout() {
        val provider = authManager.oauthProvider.value

        authRemoteDataSource.signOut()
            .onFailure { error ->
                Log.w(TAG, "logout: Backend sign-out failed", error)
            }

        when (provider) {
            OAuthProvider.KAKAO -> {
                kakaoAuthDataSource.logout()
                    .onFailure { error ->
                        Log.w(TAG, "logout: Kakao SDK logout failed", error)
                    }
            }

            OAuthProvider.GOOGLE -> {
                // MARK(@이대근): Google login not implemented yet 2025.10.04.
                Unit
            }

            null -> {
                Unit
            }
        }

        authManager.clearTokens()
    }

    // ========== 회원가입 ==========

    suspend fun signUp(signUpData: SignUpData): Result<Unit> {
        val tokenResult = authRemoteDataSource.signUp(signUpData = signUpData)
        val failure = tokenResult.exceptionOrNull()?.let(::mapSignUpFailure)

        if (failure != null) {
            return Result.failure(exception = failure)
        }

        val token = tokenResult.getOrThrow()
        return runCatching {
            authManager.saveServerToken(
                accessToken = token.accessToken,
                refreshToken = token.refreshToken,
                userId = token.userId,
            )
        }.fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { exception ->
                if (exception is CancellationException) {
                    throw exception
                }
                Result.failure(exception = exception)
            },
        )
    }

    private fun mapSignUpFailure(throwable: Throwable): Throwable {
        return if (throwable is HttpException) {
            FeelinServerException(description = throwable.toServerErrorDto())
        } else {
            throwable
        }
    }

    // ========== 회원탈퇴 ==========

    @Suppress("ReturnCount") // TODO(@이대근): 구글 로그인 구현 이후 어노테이션 삭제 2026.03.23.
    suspend fun deleteAccount(): Result<Unit> {
        authRemoteDataSource.deleteAccount().onFailure {
            return Result.failure(exception = it)
        }

        when (authManager.oauthProvider.value) {
            OAuthProvider.KAKAO -> {
                kakaoAuthDataSource.unlink().onFailure {
                    return Result.failure(exception = it)
                }
            }

            OAuthProvider.GOOGLE -> {
                TODO("@이대근 구글 로그인 시 카카오의 언링크와 동일한 기능이 있을 시 연동 2025.10.12.")
            }

            null -> {
                return Result.failure(exception = IllegalStateException("OAuth provider is null"))
            }
        }

        authManager.clearTokens()

        return Result.success(Unit)
    }
}

private fun Throwable.toServerErrorCode(): String {
    return when (this) {
        is AuthTokenRefreshException -> errorCode ?: UNKNOWN_SERVER_ERROR_CODE
        is HttpException -> toServerErrorDto().errorCode
        else -> UNKNOWN_SERVER_ERROR_CODE
    }
}

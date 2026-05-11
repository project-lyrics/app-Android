package com.lyrics.feelin.core.data.manager

import com.lyrics.feelin.core.data.datasource.remote.AuthRemoteDataSource
import com.lyrics.feelin.core.domain.model.AuthToken
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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
                        clearTokensAndFail(exception)
                    },
                )
        }
    }

    private suspend fun clearTokensAndFail(exception: Throwable): Result<AuthToken> {
        authManager.clearTokens()
        return Result.failure(exception)
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
}

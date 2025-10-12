package com.lyrics.feelin.core.data.repository

import com.lyrics.feelin.core.data.datasource.remote.AuthRemoteDataSource
import com.lyrics.feelin.core.data.datasource.remote.dto.exception.FeelinServerException
import com.lyrics.feelin.core.data.datasource.sdk.GoogleAuthDataSource
import com.lyrics.feelin.core.data.datasource.sdk.KakaoAuthDataSource
import com.lyrics.feelin.core.data.manager.AuthManager
import com.lyrics.feelin.core.domain.model.AuthToken
import com.lyrics.feelin.core.domain.model.OAuthProvider
import com.lyrics.feelin.core.domain.model.OAuthToken
import com.lyrics.feelin.core.domain.model.SignUpData
import com.lyrics.feelin.util.toServerErrorDto
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

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
    private val authManager: AuthManager
) {
    // ========== 로그인 상태 노출 ==========

    val isLoggedIn: StateFlow<Boolean> = authManager.isLoggedIn

    val userId: StateFlow<Long?> = authManager.userId

    // ========== 로그인 ==========

    /**
     * 소셜 로그인 (OAuth Provider)
     *
     * **플로우:**
     * 1. SDK를 통해 OAuth 토큰 획득 (Kakao/Google)
     * 2. Backend에 OAuth 토큰 전달 → 자체 JWT 토큰 발급
     * 3. AuthManager에 JWT 토큰 저장
     *
     * @param provider 로그인 제공자 (KAKAO, GOOGLE)
     * @return Result<Unit> 성공/실패
     */
    suspend fun login(provider: OAuthProvider): Result<Unit> {
        // 1. OAuth 토큰 획득
        val oauthToken = getOAuthToken(provider).getOrElse { error ->
            return Result.failure(exception = error)
        }

        // 2. Backend 인증 및 JWT 토큰 발급
        val authToken = authenticateWithBackend(provider, oauthToken).getOrElse { error ->
            return Result.failure(exception = error)
        }

        // 3. 토큰 저장
        saveAllTokens(authToken, oauthToken, provider)

        return Result.success(value = Unit)
    }

    /**
     * OAuth 토큰 획득
     */
    private suspend fun getOAuthToken(provider: OAuthProvider): Result<OAuthToken> {
        return when (provider) {
            OAuthProvider.KAKAO -> kakaoAuthDataSource.login()
            OAuthProvider.GOOGLE -> {
                // TODO(@이대근): Google 로그인 구현 필요. 2025.10.04.
                Result.failure(exception = NotImplementedError("Google login not implemented yet"))
            }
        }
    }

    /**
     * Backend 인증 및 JWT 토큰 발급
     *
     * HTTP_NOT_FOUND(404): 회원가입이 필요한 경우 OAuth 토큰을 임시 저장하고 예외 발생
     */
    private suspend fun authenticateWithBackend(
        provider: OAuthProvider,
        oauthToken: OAuthToken
    ): Result<AuthToken> {
        val result = authRemoteDataSource.signIn(
            provider = provider,
            oAuthToken = oauthToken,
            deviceId = ""
        ).onFailure { error ->
            if (error is HttpException) {
                when (error.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        val errorDto = error.toServerErrorDto()
                        return Result.failure(exception = FeelinServerException(description = errorDto))
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> {
                        // 회원가입 필요: OAuth 토큰만 임시 저장
                        handleRegistrationRequired(provider, oauthToken)
                        val errorDto = error.toServerErrorDto()
                        return Result.failure(exception = FeelinServerException(description = errorDto))
                    }
                }
            }
            return Result.failure(exception = error)
        }

        val response = result.getOrThrow()
        return Result.success(
            AuthToken(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                userId = response.userId
            )
        )
    }

    /** 회원가입이 필요한 경우 OAuth 토큰 임시 저장 */
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
     * 1. Backend에 로그아웃 요청 (옵션)
     * 2. SDK 로그아웃 (Kakao/Google)
     * 3. AuthManager에서 토큰 삭제
     */
    @Suppress("ReturnCount") // TODO(@이대근): 구글 로그인 구현 이후 어노테이션 삭제 2025.10.04.
    suspend fun logout(): Result<Unit> {
        // 1. Backend 로그아웃
        authRemoteDataSource.signOut().onFailure {
            return Result.failure(exception = it)
        }

        // 2. SDK 로그아웃
        when (authManager.oauthProvider.value) {
            OAuthProvider.KAKAO -> {
                kakaoAuthDataSource.logout()
            }

            OAuthProvider.GOOGLE -> {
                return Result.failure(exception = NotImplementedError("Google login not implemented yet"))
            }

            null -> {
                return Result.failure(exception = IllegalStateException("OAuth provider is null"))
            }
        }

        // 3. 토큰 삭제
        authManager.clearTokens()

        return Result.success(Unit)
    }

    // ========== 회원가입 ==========

    suspend fun signUp(deviceId: String, signUpData: SignUpData): Result<Unit> {
        val tokenResult =
            authRemoteDataSource.signUp(deviceId = deviceId, signUpData = signUpData).onFailure {
                return Result.failure(exception = it)
            }

        val token = tokenResult.getOrNull()!!
        authManager.saveServerToken(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
            userId = token.userId,
        )

        return Result.success(Unit)
    }

    // ========== 회원탈퇴 ==========

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

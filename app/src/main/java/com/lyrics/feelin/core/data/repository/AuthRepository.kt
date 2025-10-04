package com.lyrics.feelin.core.data.repository

import com.lyrics.feelin.core.data.datasource.remote.AuthRemoteDataSource
import com.lyrics.feelin.core.data.datasource.sdk.GoogleAuthDataSource
import com.lyrics.feelin.core.data.datasource.sdk.KakaoAuthDataSource
import com.lyrics.feelin.core.data.manager.AuthManager
import com.lyrics.feelin.core.domain.model.AuthToken
import com.lyrics.feelin.core.domain.model.OAuthProvider
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow

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
    @Suppress("UnusedPrivateMember") // TODO(@이대근): Backend API 연동 후 어노테이션 제거할 것. 2025.10.04.
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
    @Suppress("ReturnCount") // OAuth 흐름의 조기 리턴은 가독성을 높임
    suspend fun login(provider: OAuthProvider): Result<Unit> {
        // 1. SDK로 OAuth 토큰 획득
        val oauthTokenResult = when (provider) {
            OAuthProvider.KAKAO -> kakaoAuthDataSource.login()
            OAuthProvider.GOOGLE -> {
                // TODO(@이대근): Google 로그인 구현 필요. 2025.10.04.
                return Result.failure(NotImplementedError("Google login not implemented yet"))
            }
        }

        // OAuth 토큰 획득 실패 시 조기 리턴
        val oauthToken = oauthTokenResult.getOrElse { error ->
            return Result.failure(error)
        }

        // 2. Backend에 OAuth 토큰 전달하여 자체 JWT 토큰 발급
        // TODO(@이대근): 자체 서버와의 로그인 로직 구현 필요. 2025.10.04.
        // 현재는 임시 더미 토큰으로 대체
        val authToken = AuthToken(
            accessToken = oauthToken.accessToken,
            refreshToken = oauthToken.refreshToken ?: "",
            userId = 1L // 임시 더미 ID
        )

        // 3. AuthManager에 토큰 저장 (Backend JWT + OAuth 정보)
        authManager.saveToken(
            accessToken = authToken.accessToken,
            refreshToken = authToken.refreshToken,
            userId = authToken.userId,
            oauthProvider = provider,
            oauthAccessToken = oauthToken.accessToken,
            oauthRefreshToken = oauthToken.refreshToken
        )

        return Result.success(Unit)
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
        return runCatching {
            // 1. Backend 로그아웃
            // TODO(@이대근): 자체 백엔드 로그아웃 로직 구현 필요 2025.10.04.
            // authRemoteDataSource.logout()

            // 2. SDK 로그아웃
            when (authManager.oauthProvider.value) {
                OAuthProvider.KAKAO -> {
                    kakaoAuthDataSource.logout()
                }
                OAuthProvider.GOOGLE -> {
                    return Result.failure(NotImplementedError("Google login not implemented yet"))
                }
                else -> {
                    return Result.failure(IllegalStateException("OAuth provider is null"))
                }
            }

            // 3. 토큰 삭제
            authManager.clearTokens()

            return Result.success(Unit)
        }
    }
}

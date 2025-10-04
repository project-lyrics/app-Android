package com.lyrics.feelin.core.domain.model

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * OAuth 토큰 데이터 모델 (SDK 독립적)
 *
 * 카카오, 구글 등 모든 OAuth 제공자의 토큰을 통합하는 공통 모델입니다.
 * SDK별 OAuthToken을 이 모델로 변환하여 Repository에서 사용합니다.
 *
 * **멀티모듈 전환 시:**
 * - :core:domain 모듈로 이동
 * - SDK 의존성이 전혀 없는 순수한 도메인 모델
 */
@OptIn(ExperimentalTime::class)
data class OAuthToken(
    /**
     * OAuth 액세스 토큰
     * - 카카오: com.kakao.sdk.auth.model.OAuthToken.accessToken
     * - 구글: GoogleSignInAccount.idToken 또는 Credential
     */
    val accessToken: String,

    /**
     * OAuth 리프레시 토큰 (옵션)
     * - 카카오: com.kakao.sdk.auth.model.OAuthToken.refreshToken
     * - 구글: 일부 경우에만 제공됨
     */
    val refreshToken: String? = null,

    /**
     * 토큰 만료 시각 (옵션)
     * - 카카오: accessTokenExpiresAt
     * - 구글: expirationTime
     */
    val expiresAt: Instant? = null,

    /** 토큰 스코프 (옵션) */
    val scopes: List<String>? = null
) {
    /**
     * 토큰이 만료되었는지 확인
     *
     * @param now 현재 시각 (기본값: `Clock.System.now()`)
     */
    fun isExpired(now: Instant = Clock.System.now()): Boolean {
        return expiresAt?.let { it < now } ?: false
    }

    /**
     * 토큰이 유효한지 확인
     *
     * @param now 현재 시각 (기본값: `Clock.System.now()`)
     */
    fun isValid(now: Instant = Clock.System.now()): Boolean {
        return !isExpired(now)
    }

    /**
     * 토큰 만료까지 남은 시간 계산
     *
     * @param now 현재 시각 (기본값: `Clock.System.now()`)
     * @return 남은 시간 (`Duration`), 만료된 경우 음수
     */
    fun remainingTime(now: Instant = Clock.System.now()): Duration? {
        return expiresAt?.let { it - now }
    }

    /**
     * 토큰이 곧 만료될 예정인지 확인 (기본: 5분 이내)
     *
     * @param threshold 임계값 (기본값: 5분)
     * @param now 현재 시각 (기본값: `Clock.System.now()`)
     */
    fun willExpireSoon(
        threshold: Duration = 300.seconds,
        now: Instant = Clock.System.now()
    ): Boolean {
        val remaining = remainingTime(now) ?: return false
        return remaining in Duration.ZERO..threshold
    }
}

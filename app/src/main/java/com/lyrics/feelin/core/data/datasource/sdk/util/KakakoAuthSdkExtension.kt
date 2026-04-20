package com.lyrics.feelin.core.data.datasource.sdk.util

import com.kakao.sdk.auth.model.OAuthToken as KakaoOAuthToken
import com.lyrics.feelin.core.domain.model.OAuthToken
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/** 카카오 SDK의 OAuthToken을 도메인 모델로 변환 */
@OptIn(ExperimentalTime::class)
fun KakaoOAuthToken.toDomainModel(): OAuthToken {
    return OAuthToken(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        expiresAt = this.accessTokenExpiresAt.let { expiresAt ->
            Instant.fromEpochMilliseconds(expiresAt.time)
        },
        scopes = this.scopes
    )
}

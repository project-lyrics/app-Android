package com.lyrics.feelin.core.data.repository

import com.lyrics.feelin.core.data.datasource.sdk.GoogleAuthDataSource
import com.lyrics.feelin.core.data.datasource.sdk.KakaoAuthDataSource
import com.lyrics.feelin.core.domain.model.OAuthProvider

@Suppress("UnusedPrivateMember") // TODO(@이대근): 어노테이션은 구현 중 제거할 것. 2025.10.02.
class AuthRepository(
    private val kakaoAuthDataSource: KakaoAuthDataSource,
    private val googleAuthDataSource: GoogleAuthDataSource,
) {

    // TODO(@최현정): 실제로는 비동기 처리가 필요합니다. 2025.10.02.
    fun login(provider: OAuthProvider) {
        when (provider) {
            OAuthProvider.KAKAO -> {
            }
            OAuthProvider.GOOGLE -> {
            }
        }
        TODO("Not yet implemented")
    }

    fun logout() {
        TODO("Not yet implemented")
    }
}

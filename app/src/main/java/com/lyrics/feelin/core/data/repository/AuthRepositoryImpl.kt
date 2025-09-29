package com.lyrics.feelin.core.data.repository

import com.lyrics.feelin.core.data.service.GoogleOAuthSdkService
import com.lyrics.feelin.core.data.service.KakaoOAuthSdkService
import com.lyrics.feelin.core.domain.model.OAuthProvider

class AuthRepositoryImpl(
    @Suppress("UnusedPrivateMember") // TODO(@이대근): 구현 중 제거할 것. 2025.09.29.
    private val kakaoOAuthSdkService: KakaoOAuthSdkService,
    @Suppress("UnusedPrivateMember") // TODO(@이대근): 구현 중 제거할 것. 2025.09.29.
    private val googleOAuthSdkService: GoogleOAuthSdkService,
) : AuthRepository {

    override fun login(provider: OAuthProvider) {
        when (provider) {
            OAuthProvider.KAKAO -> {
            }
            OAuthProvider.GOOGLE -> {
            }
        }
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}

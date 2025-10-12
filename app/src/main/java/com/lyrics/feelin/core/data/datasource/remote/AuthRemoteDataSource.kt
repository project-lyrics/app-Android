package com.lyrics.feelin.core.data.datasource.remote

import com.lyrics.feelin.core.data.datasource.remote.dto.ServerStatusResponseDto
import com.lyrics.feelin.core.data.datasource.remote.dto.SignInRequestDto
import com.lyrics.feelin.core.data.util.safeApiCall
import com.lyrics.feelin.core.domain.model.AuthToken
import com.lyrics.feelin.core.domain.model.OAuthProvider
import com.lyrics.feelin.core.domain.model.OAuthToken
import com.lyrics.feelin.core.domain.model.SignUpData

class AuthRemoteDataSource(
    private val authApiService: AuthApiService
) {
    suspend fun signIn(provider: OAuthProvider, oAuthToken: OAuthToken, deviceId: String): Result<AuthToken> {
        return safeApiCall {
            authApiService.signIn(
                deviceId = deviceId,
                body = SignInRequestDto(
                    socialAccessToken = oAuthToken.accessToken,
                    authProvider = provider
                )
            )
        }
    }

    suspend fun signOut(): Result<ServerStatusResponseDto> {
        return safeApiCall { authApiService.signOut() }
    }

    // TODO(@이대근): 온보딩 화면 제작하면서 연동 필요 2025.10.12.
    suspend fun signUp(deviceId: String, signUpData: SignUpData): Result<AuthToken> {
        return safeApiCall { authApiService.signUp(deviceId = deviceId, body = signUpData) }
    }

    suspend fun deleteAccount(): Result<ServerStatusResponseDto> {
        return safeApiCall { authApiService.deleteAccount() }
    }
}

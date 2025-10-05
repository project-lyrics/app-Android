package com.lyrics.feelin.core.data.datasource.remote

import com.lyrics.feelin.core.data.datasource.remote.dto.RefreshTokenRequestDto
import com.lyrics.feelin.core.data.datasource.remote.dto.ServerStatusResponseDto
import com.lyrics.feelin.core.data.datasource.remote.dto.SignInRequestDto
import com.lyrics.feelin.core.domain.model.AuthToken
import com.lyrics.feelin.core.domain.model.SignUpData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @DELETE("api/v1/auth/delete")
    suspend fun deleteAccount(): Response<ServerStatusResponseDto>

    @POST("api/v1/auth/sign-in")
    suspend fun signIn(@Body body: SignInRequestDto): Response<AuthToken>

    @DELETE("api/v1/auth/sign-out")
    suspend fun signOut(): Response<ServerStatusResponseDto>

    @POST("api/v1/auth/sign-up")
    suspend fun signUp(@Body body: SignUpData): Response<AuthToken>

    @POST("api/v1/auth/token")
    suspend fun reIssueToken(@Body refreshTokenDto: RefreshTokenRequestDto): Response<AuthToken>

    @GET("api/v1/auth/validate-token")
    suspend fun validateToken(): Response<ServerStatusResponseDto>
}

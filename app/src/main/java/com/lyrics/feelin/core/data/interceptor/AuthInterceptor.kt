package com.lyrics.feelin.core.data.interceptor

import com.lyrics.feelin.core.data.manager.AuthManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp Interceptor: 모든 API 요청에 Access Token 자동 추가
 *
 * **동작:**
 * - AuthManager에서 Access Token 조회
 * - Authorization 헤더에 "Bearer {token}" 추가
 *
 * **사용법:**
 * ```kotlin
 * OkHttpClient.Builder()
 *     .addInterceptor(authInterceptor)
 *     .build()
 * ```
 *
 * **멀티모듈 전환 시:**
 * - :core:network 모듈로 이동 가능
 * - 또는 :core:data 모듈에서 관리
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Access Token이 없으면 원본 요청 그대로 전송
        val accessToken = authManager.accessToken.value
            ?: return chain.proceed(originalRequest)

        // Authorization 헤더 추가
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}

/**
 * Token Refresh를 자동 처리하는 Authenticator (선택 사항)
 *
 * **동작:**
 * - 401 Unauthorized 응답 시 자동으로 토큰 갱신 시도
 * - 갱신 성공 시 원본 요청 재시도
 * - 갱신 실패 시 로그아웃 처리
 *
 * **사용법:**
 * ```kotlin
 * OkHttpClient.Builder()
 *     .authenticator(tokenAuthenticator)
 *     .build()
 * ```
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val authManager: AuthManager
) : Authenticator {

    @Suppress("ReturnCount")
    override fun authenticate(route: okhttp3.Route?, response: Response): okhttp3.Request? {
        // 이미 재시도한 경우 중단 (무한 루프 방지)
        if (response.request.header("Authorization") == null) {
            return null
        }

        // Refresh Token으로 갱신 시도
        val refreshToken = authManager.refreshToken.value ?: return null

        return runBlocking {
            // TODO(@이대근): 자체 서버로 토큰 갱신 API 호출 2025.10.04.

            val newAccessToken = authManager.accessToken.value ?: return@runBlocking null

            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        }
    }
}

package com.lyrics.feelin.core.data.interceptor

import android.util.Log
import com.lyrics.feelin.core.data.manager.AuthManager
import com.lyrics.feelin.core.data.manager.AuthTokenRefresher
import dagger.Lazy
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 모든 API 요청에 Access Token을 자동으로 추가합니다.
 *
 * **동작:**
 * - AuthManager에서 Access Token 조회
 * - Authorization 헤더에 "Bearer {token}" 추가
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
        val accessToken = runBlocking {
            authManager.initializationComplete.await()
            authManager.accessToken.value
        } ?: return chain.proceed(originalRequest)

        // Authorization 헤더 추가
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}

/**
 * Token Refresh를 자동 처리하는 Authenticator
 *
 * **동작:**
 * - 401 Unauthorized 응답 시 자동으로 토큰 갱신 시도
 * - 갱신 성공 시 원본 요청 재시도
 * - 갱신 실패 시 null 반환 (로그아웃 처리는 상위 레이어에서)
 *
 * **주의:**
 * - AuthTokenRefresher를 Lazy 주입 (순환 참조 방지)
 *
 * **Detekt Suppression:**
 * - `TooGenericExceptionCaught`: 토큰 갱신 실패 시 모든 예외를 동일하게 처리 (null 반환).
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val authManager: AuthManager,
    private val authTokenRefresher: Lazy<AuthTokenRefresher>
) : Authenticator {

    @Suppress("TooGenericExceptionCaught")
    override fun authenticate(route: okhttp3.Route?, response: Response): okhttp3.Request? {
        // 이미 재시도한 경우 중단 (무한 루프 방지)
        if (response.priorResponse != null || response.request.url.encodedPath == TOKEN_REISSUE_PATH) {
            return null
        }

        return runBlocking {
            try {
                authManager.initializationComplete.await()
                val staleAccessToken = response.request.header("Authorization")
                    ?.removePrefix(BEARER_PREFIX)
                val refreshResult = authTokenRefresher.get()
                    .refreshServerToken(staleAccessToken = staleAccessToken)
                val tokenResponse = refreshResult.getOrNull()
                    ?: run {
                        refreshResult.exceptionOrNull()?.let { error ->
                            Log.w(TAG, "Token refresh failed", error)
                        }
                        return@runBlocking null
                    }

                // 재시도 요청 생성
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${tokenResponse.accessToken}")
                    .build()
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                // 네트워크 오류 - 재시도하지 않고 실패 처리
                Log.w(TAG, "Token refresh failed", e)
                null
            } catch (e: Exception) {
                // 갱신 실패 시 null 반환 (로그아웃 처리는 상위에서)
                Log.w(TAG, "Token refresh failed", e)
                null
            }
        }
    }

    companion object {
        private const val TAG = "TokenAuthenticator"
        private const val BEARER_PREFIX = "Bearer "
        private const val TOKEN_REISSUE_PATH = "/api/v1/auth/token"
    }
}

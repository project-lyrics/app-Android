package com.lyrics.feelin.core.data.datasource.sdk

import android.util.Log
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
class KakaoAuthDataSource {
    suspend fun logout(): Result<Unit> = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.logout { error ->
            when {
                error != null -> {
                    // 에러 타입별 로깅
                    if (error is ClientError) {
                        when (error.reason) {
                            ClientErrorCause.TokenNotFound -> {
                                Log.w(TAG, "logout: Token not found - user may already be logged out", error)
                            }
                            ClientErrorCause.BadParameter -> {
                                Log.e(TAG, "logout: Bad parameter - check app configuration", error)
                            }
                            else -> {
                                Log.w(TAG, "logout: Client error - ${error.reason}", error)
                            }
                        }
                    } else {
                        Log.e(TAG, "logout: Server or network error", error)
                    }
                    continuation.resume(Result.failure(error))
                }
                else -> {
                    Log.d(TAG, "logout: Successfully logged out")
                    continuation.resume(Result.success(Unit))
                }
            }
        }
    }

    /** 카카오 계정을 Feelin과 언링크합니다. 회원 탈퇴 시 호출할 수 있도록 합니다. */
    suspend fun unlink(): Result<Unit> = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.unlink { error ->
            when {
                error != null -> {
                    // 에러 타입별 로깅
                    if (error is ClientError) {
                        when (error.reason) {
                            ClientErrorCause.TokenNotFound -> {
                                Log.w(TAG, "unlink: Token not found - user may already be unlinked", error)
                            }
                            ClientErrorCause.BadParameter -> {
                                Log.e(TAG, "unlink: Bad parameter - check app configuration", error)
                            }
                            else -> {
                                Log.w(TAG, "unlink: Client error - ${error.reason}", error)
                            }
                        }
                    } else {
                        // 네트워크/서버 에러 - 재시도 가능한 에러
                        Log.e(TAG, "unlink: Server or network error - retry may be needed", error)
                    }
                    continuation.resume(Result.failure(error))
                }
                else -> {
                    Log.d(TAG, "unlink: Successfully unlinked")
                    continuation.resume(Result.success(Unit))
                }
            }
        }
    }

    companion object {
        private const val TAG = "KakaoAuthDataSource"
    }
}

package com.lyrics.feelin.core.data.datasource.sdk

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

class KakaoAuthDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val kakaoSdkClient: UserApiClient = UserApiClient.instance
) {
    suspend fun login(): Result<OAuthToken> = suspendCancellableCoroutine { continuation ->
        if (kakaoSdkClient.isKakaoTalkLoginAvailable(context)) {
            loginWithKakaoTalk(continuation)
        } else {
            loginWithKakaoAccount(continuation)
        }
    }

    suspend fun logout(): Result<Unit> = suspendCancellableCoroutine { continuation ->
        kakaoSdkClient.logout { error ->
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
        kakaoSdkClient.unlink { error ->
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

    private fun loginWithKakaoTalk(
        continuation: CancellableContinuation<Result<OAuthToken>>
    ) {
        kakaoSdkClient.loginWithKakaoTalk(context) { token, error ->
            when {
                error != null -> {
                    // ClientError가 아닌 경우 (서버 에러, 네트워크 에러 등)
                    if (error !is ClientError) {
                        continuation.resume(Result.failure(error))
                        return@loginWithKakaoTalk
                    }

                    // ClientError 타입별 처리
                    when (error.reason) {
                        // 사용자가 명시적으로 취소한 경우 - 폴백 없이 실패 반환
                        ClientErrorCause.Cancelled -> {
                            continuation.resume(Result.failure(error))
                        }

                        // 지원하지 않는 기능 - 폴백 없이 실패 반환
                        ClientErrorCause.NotSupported -> {
                            continuation.resume(Result.failure(error))
                        }

                        // 잘못된 파라미터 - 폴백 없이 실패 반환 (코드 수정 필요)
                        ClientErrorCause.BadParameter -> {
                            Log.e(
                                TAG,
                                "loginWithKakaoTalk: kakao login failure with wrong param",
                                error
                            )
                            continuation.resume(Result.failure(error))
                        }

                        // 그 외의 경우 (Unknown, TokenNotFound, IllegalState 등) - 카카오계정으로 폴백
                        else -> {
                            loginWithKakaoAccount(continuation)
                        }
                    }
                }
                token != null -> {
                    continuation.resume(Result.success(token))
                }
            }
        }
    }

    private fun loginWithKakaoAccount(
        continuation: CancellableContinuation<Result<OAuthToken>>
    ) {
        kakaoSdkClient.loginWithKakaoAccount(context) { token, error ->
            when {
                error != null -> {
                    // ClientError 타입별 로깅
                    if (error is ClientError) {
                        when (error.reason) {
                            ClientErrorCause.Cancelled -> {
                                Log.d(TAG, "loginWithKakaoAccount: User cancelled login")
                            }

                            ClientErrorCause.BadParameter -> {
                                Log.e(
                                    TAG,
                                    "loginWithKakaoAccount: kakao login failure with wrong param",
                                    error
                                )
                            }

                            else -> {
                                Log.w(TAG, "loginWithKakaoAccount: Client error - ${error.reason}", error)
                            }
                        }
                    } else {
                        Log.e(TAG, "loginWithKakaoAccount: Server or network error", error)
                    }
                    continuation.resume(Result.failure(error))
                }
                token != null -> {
                    continuation.resume(Result.success(token))
                }
            }
        }
    }

    companion object {
        private const val TAG = "KakaoAuthDataService"
    }
}

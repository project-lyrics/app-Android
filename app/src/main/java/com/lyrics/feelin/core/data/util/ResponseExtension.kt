package com.lyrics.feelin.core.data.util

import java.io.IOException
import retrofit2.HttpException
import retrofit2.Response

/**
 * Retrofit API 호출을 안전하게 래핑
 *
 * **처리하는 예외:**
 * - IOException: 네트워크 오류 (타임아웃, 연결 실패, DNS 오류 등)
 * - HttpException: HTTP 에러 응답 (4xx, 5xx)
 * - IllegalStateException: 서버에서 성공한 응답의 본문이 null인 경우
 *
 * **사용법:**
 * ```kotlin
 * suspend fun getData(): Result<Data> {
 *     return safeApiCall { authApiService.getData() }
 * }
 * ```
 *
 * @param apiCall Retrofit suspend 함수 호출
 * @return 성공시 Result.success(body), 에러 발생시 Result.failure(exception)
 */
suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): Result<T> {
    return try {
        apiCall().toResult()
    } catch (e: IOException) {
        Result.failure(e)
    }
}

/**
 * Retrofit Response를 안전하게 Result로 변환
 *
 * **사용법:**
 * ```kotlin
 * suspend fun getData(): Result<Data> {
 *     return authApiService.getData().toResult()
 * }
 * ```
 *
 * @return Result.success(body) if successful and body is not null
 * @return Result.failure(HttpException) if HTTP error (4xx, 5xx)
 * @return Result.failure(IllegalStateException) if body is null on successful response (server error)
 */
private fun <T> Response<T>.toResult(): Result<T> {
    return if (isSuccessful) {
        val body = body()
        if (body != null) {
            Result.success(body)
        } else {
            Result.failure(
                IllegalStateException(
                    "Server returned null body for successful response (code: ${code()})"
                )
            )
        }
    } else {
        Result.failure(HttpException(this))
    }
}

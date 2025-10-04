package com.lyrics.feelin.core.domain.model

/**
 * 자체 백엔드 서버 계정 인증 토큰 데이터 모델
 *
 * **백엔드 응답 구조:**
 * ```json
 * {
 *   "accessToken": JWT Access Token,
 *   "refreshToken": JWT Refresh Token,
 *   "userId": 정수형의 사용자 ID
 * }
 * ```
 */
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long
)

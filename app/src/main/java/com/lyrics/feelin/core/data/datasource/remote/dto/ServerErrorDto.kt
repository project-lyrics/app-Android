package com.lyrics.feelin.core.data.datasource.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * 서버 에러 응답 모델
 *
 * 서버의 [ErrorResponse.java]와 매핑되는 클라이언트 모델입니다.
 * 상세한 에러 코드 목록은 [노션 문서](https://www.notion.so/22d546f268d5815394f3fbf4f4e07300)를 참조하세요.
 *
 * @property errorCode 커스텀 에러 코드 (예: "00009", "01004" 등)
 * @property errorMessage 에러 메시지 (도메인 에러의 경우 UI에 표시 권장)
 * @property data 추가 데이터 (대부분 null, 특정 에러에서만 사용)
 *
 * ## data 필드 사용 케이스
 *
 * 현재 서버에서 [data]에 실제 값을 담아 보내는 경우는 **단 1가지**입니다:
 *
 * ### 1. 앱 업데이트 필수 (errorCode: "00009")
 * ```json
 * {
 *   "errorCode": "00009",
 *   "errorMessage": "최신 버전이 아니므로 업데이트가 필요합니다.",
 *   "data": {
 *     "appStoreUrl": "https://apps.apple.com/kr/app/6738319829"
 *   }
 * }
 * ```
 */
@Serializable
data class ServerErrorDto(
    val errorCode: String,
    val errorMessage: String,
    val data: JsonElement? = null
)
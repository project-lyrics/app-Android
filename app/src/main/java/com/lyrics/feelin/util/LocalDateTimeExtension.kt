package com.lyrics.feelin.util

import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

private const val YEAR_100 = 100

/**
 * 현재 시간과 비교하여 사용자에게 친화적인 시간 표현을 반환합니다.
 *
 * @return 시간 차이에 따른 사용자 친화적 문자열
 * - 10분 이내: "방금"
 * - 10분 ~ 1시간 이내: "xx분 전"
 * - 1시간 ~ 24시간 이내: "xx시간 전"
 * - 24시간 ~ 7일 이내: "xx일 전"
 * - 7일 이상 또는 미래 시간: "M/D HH:MM" 형식의 날짜 시:분
 *
 * @see kotlinx.datetime.LocalDateTime 비교 기능을 활용하여 직접 비교 수행
 * @see kotlin.time.Duration 를 통한 정밀한 시간 간격 계산
 * @author 이대근(w. Claude 4 Sonnet)
 * @since 2025.08.17
 */
@OptIn(ExperimentalTime::class) // MARK(@이대근): kotlin.time.Clock 2025.08.17.
fun LocalDateTime.compareNowToUser(): String {
    val now: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val timeZone = TimeZone.currentSystemDefault()

    // 미래의 시간인 경우 처리 - 실제 비즈니스에서는 발생하지 않아야 함
    if (this > now) {
        return this.toDisplayFormat()
    }

    // 과거 시간과의 차이 계산
    val thisInstant = this.toInstant(timeZone)
    val nowInstant = now.toInstant(timeZone)
    val duration = nowInstant - thisInstant

    return when {
        duration < 10.minutes -> "방금"
        duration < 1.hours -> "${duration.inWholeMinutes}분 전"
        duration < 24.hours -> "${duration.inWholeHours}시간 전"
        duration < 7.days -> "${duration.inWholeDays}일 전"
        else -> this.toDisplayFormat()
    }
}

/**
 * LocalDateTime을 "YY.MM.DD HH:mm" 형식의 사용자 친화적 문자열로 변환합니다.
 *
 * @return "YY.MM.DD HH:mm" 형식의 문자열 (예: "24.12.25 14:30")
 */
fun LocalDateTime.toDisplayFormat(): String {
    val yearShort = (year % YEAR_100).toString().padStart(2, '0')
    val monthStr = month.number.toString().padStart(2, '0')
    val dayStr = day.toString().padStart(2, '0')
    val hourStr = hour.toString().padStart(2, '0')
    val minuteStr = minute.toString().padStart(2, '0')
    return "$yearShort.$monthStr.$dayStr $hourStr:$minuteStr"
}

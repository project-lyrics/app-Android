package com.lyrics.feelin.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.lyrics.feelin.R

/** [프리텐다드 폰트](https://cactus.tistory.com/306) 패밀리 설정입니다. */
val pretendardFamily =
    FontFamily(
        Font(R.font.pretendard_thin, FontWeight.Thin),
        Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
        Font(R.font.pretendard_light, FontWeight.Light),
        Font(R.font.pretendard_regular, FontWeight.Normal),
        Font(R.font.pretendard_medium, FontWeight.Medium),
        Font(R.font.pretendard_semibold, FontWeight.SemiBold),
        Font(R.font.pretendard_bold, FontWeight.Bold),
        Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
        Font(R.font.pretendard_black, FontWeight.Black),
    )

// FIXME(@이대근): 타이포그래피에 임의로 px-sp를 1:1로 대응함 2025.08.11.
// 기준 합의가 있으면 1:1로 둬도 됨:
// 디자인이 기본 스케일(폰트 크기 1.0, mdpi 기준)을 가정했다면,
// 예를 들어 16px → 16.sp처럼 그대로 두는 것이 일반적이고 권장됩니다.
// 런타임에서 기기마다 적절히 스케일됩니다.

/**
 * M3 Typography에 피그마 디자인 중 Typography를 대응한 것입니다.
 *
 * M3에 바로 대응되지 않는 Caption1, Caption2는 각각 LabelSmall, LabelMedium에 대입했습니다.
 */
val Typography =
    Typography(
        headlineLarge =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 30.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        // caption1 대응(가장 작은 레이블)
        labelSmall =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                lineHeight = 16.sp,
            ),
        // caption2 대응(그 다음 크기 레이블)
        labelMedium =
            TextStyle(
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
    )

/**
 * 피그마 Typography의 Active 설정입니다.
 *
 * M3에 대응되지 않아 별도의 최상위 변수로 선언합니다.
 */
val CaptionActiveTextStyle =
    Typography.labelSmall.copy(textDecoration = TextDecoration.Underline, lineHeight = 20.sp)

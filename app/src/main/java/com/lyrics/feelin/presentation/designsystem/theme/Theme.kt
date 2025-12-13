package com.lyrics.feelin.presentation.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

/**
 * 앱의 기본 다크 베이스 스킴입니다.
 *
 * 동적 컬러가 비활성화되었거나 Android 12 미만일 때 사용되는 기본값이며, 실제 제공 시에는 [resolvedColorScheme]에서 브랜드/표면 역할을 일부
 * 오버라이드합니다.
 */
private val DarkColorScheme =
    darkColorScheme(
        primary = DarkBrandPrimary,
        secondary = DarkBrandSecondary,
        tertiary = DarkBrandTertiary,
    )

/**
 * 앱의 기본 라이트 베이스 스킴입니다.
 *
 * 동적 컬러가 비활성화되었거나 Android 12 미만일 때 사용되는 기본값이며, 실제 제공 시에는 [resolvedColorScheme]에서 브랜드/표면 역할을 일부
 * 오버라이드합니다.
 */
private val LightColorScheme =
    lightColorScheme(
        primary = LightBrandPrimary,
        secondary = LightBrandSecondary,
        tertiary = LightBrandTertiary,
    )

/**
 * 시스템/동적 팔레트를 베이스로 받고, 브랜드와 표면을 `Colors`의 디자인시스템 색상으로 오버라이드해 최종 ColorScheme을 만듭니다.
 *
 * 베이스 선택:
 * - Android 12+: 동적 팔레트(dark/light)
 * - 그 외: 정적 베이스([DarkColorScheme]/[LightColorScheme])
 *
 * 오버라이드되는 역할:
 * - primary: 브랜드 프라이머리
 * - primaryContainer: 배경 Primary (라이트/다크에 맞춤)
 * - secondary: 브랜드 세컨더리
 * - secondaryContainer: 배경 Secondary
 * - tertiary: 브랜드 터셔리
 * - tertiaryContainer: 배경 Tertiary
 * - outline: 시스템 보더
 *
 * 동적 팔레트 사용 시에도 위 역할은 앱 브랜드/디자인 토큰을 우선합니다.
 *
 * @param darkTheme 시스템 다크 모드 여부
 * @param dynamicColor Android 12+ 동적 컬러 사용 여부
 * @return 오버라이드가 반영된 최종 ColorScheme
 */
@Composable
private fun resolvedColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
): ColorScheme {
    val base =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    return base.copy(
        primary = if (darkTheme) DarkBrandPrimary else LightBrandPrimary,
        primaryContainer = if (darkTheme) DarkBackgroundPrimary else LightBackgroundPrimary,
        secondary = if (darkTheme) DarkBrandSecondary else LightBrandSecondary,
        secondaryContainer = if (darkTheme) DarkBackgroundSecondary else LightBackgroundSecondary,
        tertiary = if (darkTheme) DarkBrandTertiary else LightBrandTertiary,
        tertiaryContainer = if (darkTheme) DarkBackgroundTertiary else LightBackgroundTertiary,
        outline = if (darkTheme) DarkSystemBorder else LightSystemBorder,
    )
}

/**
 * 앱 전역 테마 엔트리입니다.
 * - 머티리얼 기반의 컴포저블 호환성을 위해 Material3 [ColorScheme]을 [resolvedColorScheme]로 제공합니다.
 * - 앱의 디자인시스템 색상 토큰을 [LocalFeelinColors]로 제공합니다.
 *
 * @param darkTheme 시스템 다크 모드 우선 여부(기본: 시스템 설정)
 * @param dynamicColor Android 12+에서 동적 컬러 사용 여부(기본: true)
 */
@Composable
fun FeelinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = resolvedColorScheme(darkTheme = darkTheme, dynamicColor = dynamicColor)

    val appColors =
        if (darkTheme) {
            FeelinColors(
                brandPrimary = DarkBrandPrimary,
                brandSecondary = DarkBrandSecondary,
                brandTertiary = DarkBrandTertiary,
                backgroundPrimary = DarkBackgroundPrimary,
                backgroundSecondary = DarkBackgroundSecondary,
                backgroundTertiary = DarkBackgroundTertiary,
                modal = DarkSystemModal,
                dim = CommonSystemDim,
                point = CommonPoint,
                inputField = DarkSystemInputField,
                systemActivate = DarkSystemActivate,
                systemDisable = DarkSystemDisable,
                systemPressedBrand = DarkSystemPressedBrand,
                systemPressedGreyScale = DarkSystemPressedGreyScale,
                alertWarning = CommonAlertWarning,
                alertSuccess = CommonAlertSuccess,
                border = DarkSystemBorder,
                gray00 = DarkGray00,
                gray01 = DarkGray01,
                gray02 = DarkGray02,
                gray03 = DarkGray03,
                gray04 = DarkGray04,
                gray05 = DarkGray05,
                gray06 = DarkGray06,
                gray07 = DarkGray07,
                gray08 = DarkGray08,
                gray09 = DarkGray09,
            )
        } else {
            FeelinColors(
                brandPrimary = LightBrandPrimary,
                brandSecondary = LightBrandSecondary,
                brandTertiary = LightBrandTertiary,
                backgroundPrimary = LightBackgroundPrimary,
                backgroundSecondary = LightBackgroundSecondary,
                backgroundTertiary = LightBackgroundTertiary,
                modal = LightSystemModal,
                dim = CommonSystemDim,
                point = CommonPoint,
                inputField = LightSystemInputField,
                systemActivate = LightSystemActivate,
                systemDisable = LightSystemDisable,
                systemPressedBrand = LightSystemPressedBrand,
                systemPressedGreyScale = LightSystemPressedGreyScale,
                alertWarning = CommonAlertWarning,
                alertSuccess = CommonAlertSuccess,
                border = LightSystemBorder,
                gray00 = LightGray00,
                gray01 = LightGray01,
                gray02 = LightGray02,
                gray03 = LightGray03,
                gray04 = LightGray04,
                gray05 = LightGray05,
                gray06 = LightGray06,
                gray07 = LightGray07,
                gray08 = LightGray08,
                gray09 = LightGray09,
            )
        }

    CompositionLocalProvider(LocalFeelinColors provides appColors) {
        MaterialTheme(colorScheme = colorScheme, typography = MaterialCompatibleTypography, content = content)
    }
}

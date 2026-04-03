package com.lyrics.feelin.presentation.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LightBrandPrimary = Color(0xFF6790F4)
val LightBrandSecondary = Color(0xFFEBF3FF)
val LightBrandTertiary = Color(0xFF20368D)
val DarkBrandPrimary = Color(0xFF658CEA)
val DarkBrandSecondary = Color(0xFF91B3E3)
val DarkBrandTertiary = Color(0xFF142875)

val LightBackgroundPrimary = Color(0xFFFFFFFF)
val LightBackgroundSecondary = Color(0xFFD2D4DA)
val LightBackgroundTertiary = Color(0xFFF3F4F8)
val DarkBackgroundPrimary = Color(0xFF0C0C0D)
val DarkBackgroundSecondary = Color(0xFF36363F)
val DarkBackgroundTertiary = Color(0xFF2A2A2E)

val LightSystemModal = Color(0xFFFFFFFF)
val DarkSystemModal = Color(0xFF494955)

val CommonSystemDim = Color(0x10122366)

val CommonPoint = Color(0xFFFA5454)

val LightSystemInputField = Color(0xFFF3F4F8)
val DarkSystemInputField = Color(0xFF222225)

val LightSystemActivate = Color(0xFF6790F4)
val DarkSystemActivate = Color(0xFF658CEA)
val LightSystemDisable = Color(0xFFD2D4DA)
val DarkSystemDisable = Color(0xFF36363F)

val LightSystemPressedBrand = Color(0xFFEBF3FF)
val DarkSystemPressedBrand = Color(0xFF91B3E3)
val LightSystemPressedGreyScale = Color(0xFFF3F4F8)
val DarkSystemPressedGreyScale = Color(0xFF6B6B79)

val CommonAlertWarning = Color(0xFFF24242)
val CommonAlertSuccess = Color(0xFF3C75FF)

val LightSystemBorder = Color(0xFF101223)
val DarkSystemBorder = Color(0xFF36363F)

val LightGray00 = Color(0xFFFFFFFF)
val LightGray01 = Color(0xFFF3F4F8)
val LightGray02 = Color(0xFFD2D4DA)
val LightGray03 = Color(0xFFB3B5BD)
val LightGray04 = Color(0xFF9496A1)
val LightGray05 = Color(0xFF777986)
val LightGray06 = Color(0xFF5B5D6B)
val LightGray07 = Color(0xFF404252)
val LightGray08 = Color(0xFF282A3A)
val LightGray09 = Color(0xFF101223)
val DarkGray00 = Color(0xFF0C0C0D)
val DarkGray01 = Color(0xFF2A2A2E)
val DarkGray02 = Color(0xFF36363F)
val DarkGray03 = Color(0xFF494955)
val DarkGray04 = Color(0xFF6B6B79)
val DarkGray05 = Color(0xFF747481)
val DarkGray06 = Color(0xFF868691)
val DarkGray07 = Color(0xFF9292A0)
val DarkGray08 = Color(0xFFB7B7C5)
val DarkGray09 = Color(0xFFDCDCE6)

const val KAKAO_YELLOW = 0xFFFFE400

/**
 * Feelin 디자인시스템의 색 토큰입니다.
 *
 * M3와 독립되게 자체 디자인시스템의 색을 관리합니다.
 *
 * @property modal 모달 배경 색
 * @property inputField 입력 필드 배경 색
 * @property point 강조/포인트 색(컴포넌트 내부에서 보조 포인트로 사용)
 * @property border 경계/디바이더 색
 * @property dim 스크림/딤 색
 * @property gray00~gray09 그레이스케일 색상 팔레트
 * @property systemActivate 시스템 활성화 색
 * @property systemDisable 시스템 비활성화 색
 * @property systemPressedBrand 브랜드 눌림 상태 색
 * @property systemPressedGreyScale 그레이스케일 눌림 상태 색
 * @property alertWarning 경고 알림 색
 * @property alertSuccess 성공 알림 색
 */
@Stable
data class FeelinColors(
    // 브랜드 색상들
    val brandPrimary: Color,
    val brandSecondary: Color,
    val brandTertiary: Color,
    // 배경(백그라운드) 색상들
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,
    // 모달 색상
    val modal: Color,
    // 딤(스크림) 색상
    val dim: Color,
    // 강조/포인트 색
    val point: Color,
    // 입력 필드 색상
    val inputField: Color,
    // 시스템 색상들
    val systemActivate: Color,
    val systemDisable: Color,
    val systemPressedBrand: Color,
    val systemPressedGreyScale: Color,
    val alertWarning: Color,
    val alertSuccess: Color,
    // 구분선(경계/디바이더) 색상
    val border: Color,
    // 그레이스케일 색상들
    val gray00: Color,
    val gray01: Color,
    val gray02: Color,
    val gray03: Color,
    val gray04: Color,
    val gray05: Color,
    val gray06: Color,
    val gray07: Color,
    val gray08: Color,
    val gray09: Color,
)

/** [FeelinColors]를 제공/소비하기 위한 CompositionLocal. */
val LocalFeelinColors =
    staticCompositionLocalOf {
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

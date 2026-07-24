package com.lyrics.feelin.core.designsystem.component

import android.content.res.Configuration
import android.graphics.Color
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.view.ContextThemeWrapper
import androidx.test.core.app.ApplicationProvider
import com.lyrics.feelin.R
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * [FeelinBirthYearPicker]가 사용하는 네이티브 [NumberPicker]가 다크모드에서 밝은 글자색으로 그려지는지 검증합니다.
 *
 * 배경: 앱 테마에 values-night 변형이 없을 때 다크모드에서 피커 숫자가 어두운 색으로 렌더링되어 보이지 않는 문제가 있었습니다.
 * (관련 이슈: #44 - theme.xml 다크모드 미대응)
 */
class BirthYearPickerDarkThemeTest {

    @Test
    fun numberPickerUsesLightTextColorInNightMode() {
        val inputText = createPickerInputText(
            uiMode = Configuration.UI_MODE_NIGHT_YES,
            styleRes = R.style.Theme_Feelin
        )

        assertTrue(
            "NumberPicker text color should be light in night mode, but was #${Integer.toHexString(inputText.currentTextColor)}",
            isLightColor(inputText.currentTextColor)
        )
    }

    @Test
    fun numberPickerUsesDarkTextColorInLightMode() {
        val inputText = createPickerInputText(
            uiMode = Configuration.UI_MODE_NIGHT_NO,
            styleRes = R.style.Theme_Feelin
        )

        assertTrue(
            "NumberPicker text color should be dark in light mode, but was #${Integer.toHexString(inputText.currentTextColor)}",
            !isLightColor(inputText.currentTextColor)
        )
    }

    /**
     * 온보딩처럼 Compose에서 라이트를 강제하는 화면을 모사합니다.
     * 시스템이 다크 모드여도 명시적 라이트 테마를 쓰면 피커 글자색이 어두워야 합니다.
     */
    @Test
    fun numberPickerWithExplicitLightThemeIgnoresSystemNightMode() {
        val inputText = createPickerInputText(
            uiMode = Configuration.UI_MODE_NIGHT_YES,
            styleRes = R.style.Theme_Feelin_Light
        )

        assertTrue(
            "NumberPicker with explicit light theme should use dark text even in night mode, " +
                "but was #${Integer.toHexString(inputText.currentTextColor)}",
            !isLightColor(inputText.currentTextColor)
        )
    }

    @Test
    fun numberPickerWithExplicitDarkThemeIgnoresSystemLightMode() {
        val inputText = createPickerInputText(
            uiMode = Configuration.UI_MODE_NIGHT_NO,
            styleRes = R.style.Theme_Feelin_Dark
        )

        assertTrue(
            "NumberPicker with explicit dark theme should use light text even in light mode, " +
                "but was #${Integer.toHexString(inputText.currentTextColor)}",
            isLightColor(inputText.currentTextColor)
        )
    }

    private fun createPickerInputText(uiMode: Int, styleRes: Int): EditText {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        val config = Configuration(context.resources.configuration).apply {
            this.uiMode = (this.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or uiMode
        }
        val configContext = context.createConfigurationContext(config)
        val themedContext = ContextThemeWrapper(configContext, styleRes)

        return findEditText(NumberPicker(themedContext))
    }

    private fun findEditText(picker: NumberPicker): EditText {
        for (i in 0 until picker.childCount) {
            val child = picker.getChildAt(i)
            if (child is EditText) return child
        }
        error("NumberPicker should contain an internal EditText")
    }

    private fun isLightColor(color: Int): Boolean {
        val luminance = (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255.0
        return luminance > 0.5
    }
}

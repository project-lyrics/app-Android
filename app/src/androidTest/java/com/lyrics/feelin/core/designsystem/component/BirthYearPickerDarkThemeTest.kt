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
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        val nightConfig = Configuration(context.resources.configuration).apply {
            uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or Configuration.UI_MODE_NIGHT_YES
        }
        val nightContext = context.createConfigurationContext(nightConfig)
        val themedContext = ContextThemeWrapper(nightContext, R.style.Theme_Feelin)

        val picker = NumberPicker(themedContext)
        val inputText = findEditText(picker)

        assertTrue(
            "NumberPicker text color should be light in night mode, but was #${Integer.toHexString(inputText.currentTextColor)}",
            isLightColor(inputText.currentTextColor)
        )
    }

    @Test
    fun numberPickerUsesDarkTextColorInLightMode() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        val lightConfig = Configuration(context.resources.configuration).apply {
            uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or Configuration.UI_MODE_NIGHT_NO
        }
        val lightContext = context.createConfigurationContext(lightConfig)
        val themedContext = ContextThemeWrapper(lightContext, R.style.Theme_Feelin)

        val picker = NumberPicker(themedContext)
        val inputText = findEditText(picker)

        assertTrue(
            "NumberPicker text color should be dark in light mode, but was #${Integer.toHexString(inputText.currentTextColor)}",
            !isLightColor(inputText.currentTextColor)
        )
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

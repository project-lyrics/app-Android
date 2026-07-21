package com.lyrics.feelin.core.designsystem.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GenderBirthYearComponentsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun genderButtonUsesFigmaHeight() {
        composeRule.setContent {
            FeelinTheme {
                FeelinGenderButton(
                    text = "여성",
                    iconRes = R.drawable.ic_gender_female_active,
                    selected = true,
                    onClick = {},
                    modifier = Modifier.testTag("genderButton"),
                )
            }
        }

        composeRule.onNodeWithTag("genderButton").assertHeightIsEqualTo(204.dp)
    }

    @Test
    fun birthYearPickerUsesFigmaHeight() {
        composeRule.setContent {
            FeelinTheme {
                FeelinBirthYearPicker(
                    value = "2000년",
                    placeholder = "출생 연도를 입력해주세요",
                    onValueChange = {},
                    modifier = Modifier.testTag("birthYearPicker"),
                )
            }
        }

        composeRule.onNodeWithTag("birthYearPicker").assertHeightIsEqualTo(52.dp)
    }

    @Test
    fun birthYearPickerKeepsSuffixedInitialValueOnConfirm() {
        var selectedYear = ""
        composeRule.setContent {
            FeelinTheme {
                FeelinBirthYearPicker(
                    value = "2000년",
                    placeholder = "출생 연도를 입력해주세요",
                    onValueChange = { selectedYear = it },
                )
            }
        }

        composeRule.onNodeWithText("2000년").performClick()
        composeRule.onNodeWithText("확인").performClick()

        composeRule.runOnIdle { assertEquals("2000", selectedYear) }
    }
}

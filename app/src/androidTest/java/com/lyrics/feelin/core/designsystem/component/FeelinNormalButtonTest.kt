package com.lyrics.feelin.core.designsystem.component

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import org.junit.Rule
import org.junit.Test

class FeelinNormalButtonTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun enabledButtonShowsTextAndAcceptsInput() {
        composeRule.setContent {
            FeelinTheme {
                FeelinNormalButton(
                    text = "완료",
                    enabled = true,
                    onClick = {},
                )
            }
        }

        composeRule.onNodeWithText("완료").assertIsEnabled()
    }

    @Test
    fun disabledButtonShowsTextAndRejectsInput() {
        composeRule.setContent {
            FeelinTheme {
                FeelinNormalButton(
                    text = "프로필 저장",
                    enabled = false,
                    onClick = {},
                )
            }
        }

        composeRule.onNodeWithText("프로필 저장").assertIsNotEnabled()
    }
}

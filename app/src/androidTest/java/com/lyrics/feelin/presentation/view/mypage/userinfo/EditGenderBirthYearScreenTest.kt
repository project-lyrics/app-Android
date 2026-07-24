package com.lyrics.feelin.presentation.view.mypage.userinfo

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditGenderBirthYearScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun initialDummyValuesAreShownAndSaveIsDisabled() {
        composeRule.setContent {
            FeelinTheme {
                EditGenderBirthYearScreen(
                    onBackClick = {},
                    onSaveClick = {},
                )
            }
        }

        composeRule.onNodeWithText("여성").assertIsDisplayed()
        composeRule.onNodeWithText("2000년").assertIsDisplayed()
        composeRule.onNodeWithText("회원 정보 저장").assertIsNotEnabled()
    }

    @Test
    fun changingGenderEnablesSaveAndInvokesCallback() {
        var saveClicked = false
        composeRule.setContent {
            FeelinTheme {
                EditGenderBirthYearScreen(
                    onBackClick = {},
                    onSaveClick = { saveClicked = true },
                )
            }
        }

        composeRule.onNodeWithText("남성").performClick()
        composeRule.onNodeWithText("회원 정보 저장").assertIsEnabled().performClick()

        composeRule.runOnIdle { assertTrue(saveClicked) }
    }

    @Test
    fun dirtyBackShowsDiscardDialogAndLeavesOnConfirm() {
        var backClicked = false
        composeRule.setContent {
            FeelinTheme {
                EditGenderBirthYearScreen(
                    onBackClick = { backClicked = true },
                    onSaveClick = {},
                )
            }
        }

        composeRule.onNodeWithText("남성").performClick()
        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.onNodeWithText("저장하지 않고 나가시겠어요?").assertIsDisplayed()
        composeRule.onNodeWithText("나가기").performClick()

        composeRule.runOnIdle { assertTrue(backClicked) }
    }
}

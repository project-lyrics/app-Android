package com.lyrics.feelin.presentation.view.mypage.userinfo

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UserInfoScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun genderBirthYearRowInvokesNavigationCallback() {
        var rowClicked = false
        composeRule.setContent {
            FeelinTheme {
                UserInfoScreen(
                    onBackClick = {},
                    onGenderBirthYearClick = { rowClicked = true },
                )
            }
        }

        composeRule.onNodeWithText("여성ㆍ2000년").performClick()

        composeRule.runOnIdle { assertTrue(rowClicked) }
    }
}

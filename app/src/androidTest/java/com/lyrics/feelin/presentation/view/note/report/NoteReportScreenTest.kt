package com.lyrics.feelin.presentation.view.note.report

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import org.junit.Rule
import org.junit.Test

class NoteReportScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun defaultScreenShowsFigmaContent() {
        setContent()

        composeTestRule.onNodeWithText("신고사유").assertIsDisplayed()
        composeTestRule.onNodeWithText("커뮤니티 성격에 맞지 않음").assertIsNotSelected()
        composeTestRule.onNodeWithText("기타").assertIsDisplayed()
        captureScreen("note-report-default.png")
    }

    @Test
    fun selectingOtherShowsSelectedStateAndInput() {
        setContent()

        composeTestRule.onNodeWithText("기타").performClick()

        composeTestRule.onNodeWithText("기타").assertIsSelected()
        composeTestRule.onNodeWithContentDescription("선택됨").assertIsDisplayed()
        composeTestRule.onNodeWithText("신고사유를 작성해주세요.").assertIsDisplayed()
        captureScreen("note-report-other-selected.png")
    }

    @Test
    fun selectingReasonAndAgreementEnablesSubmit() {
        setContent()

        composeTestRule.onNodeWithText("상업적 광고").performClick()
        composeTestRule.onNodeWithText("개인정보 수집에 동의합니다.").performClick()

        composeTestRule.onNode(hasText("신고하기") and hasClickAction())
            .assertIsEnabled()
            .performClick()
        composeTestRule.onNodeWithText("신고가 접수되었어요.").assertIsDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            FeelinTheme {
                NoteReportScreen(
                    noteId = 1L,
                    onBackClick = {},
                )
            }
        }
    }

    private fun captureScreen(fileName: String) {
        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("screencap -p /sdcard/$fileName")
            .close()
    }
}

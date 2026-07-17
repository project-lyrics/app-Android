package com.lyrics.feelin.presentation.view.notification

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import org.junit.Rule
import org.junit.Test

class NotificationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBarShowsBackWithoutEdit() {
        setContent(NotificationUiState.populatedSample())

        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithText("편집").assertDoesNotExist()
    }

    @Test
    fun emptyStateShowsText() {
        setContent(NotificationUiState.emptySample())

        composeTestRule.onNodeWithText("새로운 알림이 없어요").assertIsDisplayed()
    }

    @Test
    fun populatedStateShowsDividersExceptAfterLastItem() {
        val uiState = NotificationUiState.populatedSample()

        setContent(uiState)

        composeTestRule.onAllNodesWithTag("notificationDivider")
            .assertCountEquals(uiState.items.lastIndex)
    }

    private fun setContent(uiState: NotificationUiState) {
        composeTestRule.setContent {
            FeelinTheme {
                NotificationScreen(
                    uiState = uiState,
                    onBackClick = {},
                    onTabClick = {},
                    onNotificationClick = {},
                    onDialogConfirmClick = {},
                )
            }
        }
    }
}

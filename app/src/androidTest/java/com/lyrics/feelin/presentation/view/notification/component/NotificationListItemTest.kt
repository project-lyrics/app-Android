package com.lyrics.feelin.presentation.view.notification.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.view.notification.NotificationItemUiModel
import org.junit.Rule
import org.junit.Test

class NotificationListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun unreadItemShowsUnreadIndicator() {
        setContent(isRead = false)

        composeTestRule.onNodeWithTag(
            testTag = "notificationUnreadIndicator",
            useUnmergedTree = true,
        ).assertIsDisplayed()
    }

    @Test
    fun readItemHidesUnreadIndicator() {
        setContent(isRead = true)

        composeTestRule.onNodeWithTag(
            testTag = "notificationUnreadIndicator",
            useUnmergedTree = true,
        ).assertDoesNotExist()
    }

    private fun setContent(isRead: Boolean) {
        composeTestRule.setContent {
            FeelinTheme {
                NotificationListItem(
                    item = NotificationItemUiModel(
                        id = 1L,
                        message = "알림 메시지",
                        timeLabel = "방금 전",
                        imageUrl = null,
                        isRead = isRead,
                    ),
                    onClick = {},
                )
            }
        }
    }
}

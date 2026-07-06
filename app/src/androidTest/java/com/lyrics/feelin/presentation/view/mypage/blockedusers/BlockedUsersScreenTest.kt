package com.lyrics.feelin.presentation.view.mypage.blockedusers

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.lyrics.feelin.presentation.view.mypage.blockedusers.component.BlockedUserListItem
import com.lyrics.feelin.presentation.view.mypage.blockedusers.BlockedUserListItemData
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import org.junit.Rule
import org.junit.Test

class BlockedUsersScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun listItem_showsNicknameAndUnblockButton() {
        composeRule.setContent {
            FeelinTheme {
                BlockedUserListItem(
                    data = BlockedUserListItemData(
                        userId = 1L,
                        nickname = "username01",
                        profileImageUrl = null,
                    ),
                    onUnblockClick = {},
                )
            }
        }

        composeRule.onNodeWithText("username01").assertExists()
        composeRule.onNodeWithText("차단 해제").assertExists()
    }

    @Test
    fun blockedUsersScreen_unblockClick_showsSnackbar() {
        composeRule.setContent {
            FeelinTheme {
                BlockedUsersScreen(
                    blockedUsers = listOf(
                        BlockedUserListItemData(
                            userId = 1L,
                            nickname = "username01",
                            profileImageUrl = null,
                        ),
                    ),
                    onBackClick = {},
                )
            }
        }

        composeRule.onNodeWithText("차단 해제").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("차단 해제되었습니다").assertExists()
    }
}

package com.lyrics.feelin.presentation.view.mypage.blockedusers

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lyrics.feelin.core.designsystem.component.FeelinSnackbarHost
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.mypage.blockedusers.component.BlockedUserListItem
import kotlinx.coroutines.launch

@Composable
fun BlockedUsersScreen(
    blockedUsers: List<BlockedUserListItemData>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalFeelinColors.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FeelinTopAppBarWithBack(
                title = "차단된 유저 관리",
                onBackClick = onBackClick
            )
        },
        snackbarHost = {
            FeelinSnackbarHost(hostState = snackbarHostState)
        },
        containerColor = colors.backgroundPrimary
    ) { paddingValues ->
        if (blockedUsers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "차단된 유저가 없어요",
                    style = FeelinTypography.body2,
                    color = colors.gray04
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colors.backgroundPrimary)
            ) {
                items(
                    items = blockedUsers,
                    key = { it.userId }
                ) { user ->
                    BlockedUserListItem(
                        data = user,
                        onUnblockClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message = "차단 해제되었습니다")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockedUsersScreenPreviewListLight() {
    FeelinTheme {
        BlockedUsersScreen(
            blockedUsers = listOf(
                BlockedUserListItemData(1L, "차단된 사용자 닉네임", null),
                BlockedUserListItemData(2L, "차단된 사용자 닉네임 2", null)
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BlockedUsersScreenPreviewListDark() {
    FeelinTheme {
        BlockedUsersScreen(
            blockedUsers = listOf(
                BlockedUserListItemData(1L, "차단된 사용자 닉네임", null),
                BlockedUserListItemData(2L, "차단된 사용자 닉네임 2", null)
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockedUsersScreenPreviewEmptyLight() {
    FeelinTheme {
        BlockedUsersScreen(
            blockedUsers = emptyList(),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BlockedUsersScreenPreviewEmptyDark() {
    FeelinTheme {
        BlockedUsersScreen(
            blockedUsers = emptyList(),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockedUsersScreenPreviewSnackbarInteractive() {
    // Note: To see the snackbar in Preview, run it in Interactive Mode and click '차단 해제'
    FeelinTheme {
        BlockedUsersScreen(
            blockedUsers = listOf(
                BlockedUserListItemData(1L, "인터랙티브 모드에서 차단 해제 클릭", null)
            ),
            onBackClick = {}
        )
    }
}

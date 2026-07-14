package com.lyrics.feelin.presentation.view.notification

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.component.FeelinTab
import com.lyrics.feelin.core.designsystem.component.FeelinTabRow
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarNoBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.notification.component.NotificationEmptyState
import com.lyrics.feelin.presentation.view.notification.component.NotificationListItem

@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    onEditClick: () -> Unit,
    onTabClick: (NotificationTab) -> Unit,
    onNotificationClick: (Long) -> Unit,
    onDialogConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current

    Scaffold(
        modifier = modifier
            .background(color = feelinColors.backgroundPrimary)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)),
        topBar = {
            FeelinTopAppBarNoBack(
                title = "알림",
                centeredTitle = true,
                actions = {
                    if (uiState.items.isNotEmpty()) {
                        Text(
                            text = "편집",
                            style = FeelinTypography.body1,
                            color = feelinColors.gray09,
                            modifier = Modifier.clickable(onClick = onEditClick)
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(feelinColors.backgroundPrimary)
                .padding(contentPadding)
        ) {
            FeelinTabRow(
                selectedTabIndex = uiState.selectedTab.ordinal
            ) {
                NotificationTab.values().forEach { tab ->
                    FeelinTab(
                        selected = uiState.selectedTab == tab,
                        onClick = { onTabClick(tab) },
                        text = { Text(text = tab.label) }
                    )
                }
            }

            if (uiState.items.isEmpty()) {
                NotificationEmptyState(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(
                        items = uiState.items,
                        key = { it.id }
                    ) { item ->
                        NotificationListItem(
                            item = item,
                            onClick = onNotificationClick
                        )
                    }
                }
            }
        }

        if (uiState.isDeletedNoteDialogVisible) {
            FeelinModalDialog(
                title = "이미 삭제된 노트예요.",
                description = null,
                confirmButtonText = "확인",
                onConfirmButtonClick = onDialogConfirmClick,
                isDismissButtonEnable = false
            )
        }
    }
}

@Preview(
    name = "Notification Screen - Populated - Light",
    showBackground = true
)
@Preview(
    name = "Notification Screen - Populated - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NotificationScreenPopulatedPreview() {
    FeelinTheme {
        NotificationScreen(
            uiState = NotificationUiState.populatedSample(),
            onEditClick = {},
            onTabClick = {},
            onNotificationClick = {},
            onDialogConfirmClick = {}
        )
    }
}

@Preview(
    name = "Notification Screen - Empty - Light",
    showBackground = true
)
@Preview(
    name = "Notification Screen - Empty - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NotificationScreenEmptyPreview() {
    FeelinTheme {
        NotificationScreen(
            uiState = NotificationUiState.emptySample(),
            onEditClick = {},
            onTabClick = {},
            onNotificationClick = {},
            onDialogConfirmClick = {}
        )
    }
}

@Preview(
    name = "Notification Screen - Report - Light",
    showBackground = true
)
@Preview(
    name = "Notification Screen - Report - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NotificationScreenReportPreview() {
    FeelinTheme {
        NotificationScreen(
            uiState = NotificationUiState.reportSample(),
            onEditClick = {},
            onTabClick = {},
            onNotificationClick = {},
            onDialogConfirmClick = {}
        )
    }
}

@Preview(
    name = "Notification Screen - Deleted Note Dialog - Light",
    showBackground = true
)
@Preview(
    name = "Notification Screen - Deleted Note Dialog - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NotificationScreenDeletedNoteDialogPreview() {
    FeelinTheme {
        NotificationScreen(
            uiState = NotificationUiState.deletedNoteDialogSample(),
            onEditClick = {},
            onTabClick = {},
            onNotificationClick = {},
            onDialogConfirmClick = {}
        )
    }
}

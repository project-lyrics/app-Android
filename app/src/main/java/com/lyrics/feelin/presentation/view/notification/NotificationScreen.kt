package com.lyrics.feelin.presentation.view.notification

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.component.FeelinTab
import com.lyrics.feelin.core.designsystem.component.FeelinTabRow
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.notification.component.NotificationEmptyState
import com.lyrics.feelin.presentation.view.notification.component.NotificationListItem

@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    onBackClick: () -> Unit,
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
            FeelinTopAppBarWithBack(
                title = "알림",
                onBackClick = onBackClick,
                centeredTitle = true,
                showDivider = false,
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
                NotificationTab.entries.forEach { tab ->
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
                    itemsIndexed(
                        items = uiState.items,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        NotificationListItem(
                            item = item,
                            onClick = onNotificationClick
                        )
                        if (index != uiState.items.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.testTag("notificationDivider"),
                                thickness = 1.dp,
                                color = feelinColors.gray01
                            )
                        }
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
            onBackClick = {},
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
            onBackClick = {},
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
            onBackClick = {},
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
            onBackClick = {},
            onTabClick = {},
            onNotificationClick = {},
            onDialogConfirmClick = {}
        )
    }
}

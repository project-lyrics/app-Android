package com.lyrics.feelin.presentation.view.home.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.FeelinTextIcon
import com.lyrics.feelin.core.designsystem.icon.NotificationIconDark
import com.lyrics.feelin.core.designsystem.icon.NotificationIconLight
import com.lyrics.feelin.core.designsystem.icon.NotificationWithBadgeIconDark
import com.lyrics.feelin.core.designsystem.icon.NotificationWithBadgeIconLight
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun HomeHeader(
    hasUnreadNotification: Boolean,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current
    val isDarkTheme = isSystemInDarkTheme()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = FeelinTextIcon,
            contentDescription = "Feelin Logo",
            tint = feelinColors.gray09,
            modifier = Modifier.width(83.dp)
        )

        // TODO(@이대근): 라이트모드/다크모드 에셋 분리해 표시
        val notificationIcon = when {
            hasUnreadNotification && isDarkTheme -> NotificationWithBadgeIconDark
            hasUnreadNotification -> NotificationWithBadgeIconLight
            isDarkTheme -> NotificationIconDark
            else -> NotificationIconLight
        }

        Icon(
            imageVector = notificationIcon,
            contentDescription = "Notification",
            modifier = Modifier.clickable(onClick = onNotificationClick),
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true, name = "Home Header - Light Theme")
@Preview(
    showBackground = true,
    name = "Home Header - Dark Theme",
    backgroundColor = 0xFF00000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeHeaderPreview() {
    FeelinTheme {
        HomeHeader(
            hasUnreadNotification = false,
            onNotificationClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Home Header Unread - Light Theme")
@Preview(
    showBackground = true,
    name = "Home Header Unread - Dark Theme",
    backgroundColor = 0xFF00000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeHeaderUnreadPreview() {
    FeelinTheme {
        HomeHeader(
            hasUnreadNotification = true,
            onNotificationClick = {},
        )
    }
}

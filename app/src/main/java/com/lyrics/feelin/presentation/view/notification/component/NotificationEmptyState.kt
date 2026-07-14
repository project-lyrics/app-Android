package com.lyrics.feelin.presentation.view.notification.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun NotificationEmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "새로운 알림이 없어요",
            style = FeelinTypography.body3,
            color = LocalFeelinColors.current.gray09,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "Notification Empty State - Light", showBackground = true)
@Preview(
    name = "Notification Empty State - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NotificationEmptyStatePreview() {
    FeelinTheme {
        Box(modifier = Modifier.background(LocalFeelinColors.current.backgroundPrimary)) {
            NotificationEmptyState()
        }
    }
}

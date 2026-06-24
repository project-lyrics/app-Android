package com.lyrics.feelin.presentation.view.home.component

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.EmptyImageDarkIcon
import com.lyrics.feelin.core.designsystem.icon.EmptyImageLightIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current
    val isDark = isSystemInDarkTheme()
    val icon = if (isDark) EmptyImageDarkIcon else EmptyImageLightIcon

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Empty State",
            modifier = Modifier.size(78.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "새로운 노트가 없어요",
            style = FeelinTypography.body2,
            color = feelinColors.gray04,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, name = "Empty State - Light")
@Preview(
    showBackground = true,
    backgroundColor = 0xFF0C0C0D,
    name = "Empty State - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EmptyStatePreview() {
    FeelinTheme {
        EmptyState()
    }
}

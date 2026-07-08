package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalDarkTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun FeelinSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.padding(horizontal = 20.dp),
        snackbar = { snackbarData ->
            FeelinSnackbar(
                message = snackbarData.visuals.message
            )
        }
    )
}

@Composable
fun FeelinSnackbar(
    message: String,
    modifier: Modifier = Modifier
) {
    val isDark = LocalDarkTheme.current
    val backgroundColor = if (isDark) {
        LocalFeelinColors.current.backgroundSecondary
    } else {
        LocalFeelinColors.current.gray07
    }

    val textColor = if (isDark) {
        LocalFeelinColors.current.gray09
    } else {
        Color.White
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(start = 18.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = LocalFeelinColors.current.brandPrimary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = message,
                style = FeelinTypography.body2,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeelinSnackbarLightPreview() {
    FeelinTheme(darkTheme = false) {
        FeelinSnackbar(
            message = "차단 해제되었습니다",
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun FeelinSnackbarDarkPreview() {
    FeelinTheme(darkTheme = true) {
        FeelinSnackbar(
            message = "차단 해제되었습니다",
            modifier = Modifier.padding(20.dp)
        )
    }
}

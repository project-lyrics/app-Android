package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun FeelinCompleteButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LocalFeelinColors.current.systemActivate,
            disabledContainerColor = LocalFeelinColors.current.systemDisable,
        )
    ) {
        Text(
            text = text,
            style = FeelinTypography.title2,
            color = LocalFeelinColors.current.gray00,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeelinCompleteButtonPreview() {
    FeelinTheme {
        FeelinCompleteButton(
            text = "완료",
            enabled = true,
            onClick = {},
        )
    }
}

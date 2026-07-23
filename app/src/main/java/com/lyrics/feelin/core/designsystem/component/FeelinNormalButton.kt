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
import com.lyrics.feelin.presentation.designsystem.theme.LightGray00
import com.lyrics.feelin.presentation.designsystem.theme.LocalDarkTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

/** 디자인시스템에 접미사가 없이 선언된 버튼입니다. */
@Composable
fun FeelinNormalButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val feelinColors = LocalFeelinColors.current
    val isDarkTheme = LocalDarkTheme.current

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = feelinColors.systemActivate,
            contentColor = LightGray00,
            disabledContainerColor = feelinColors.systemDisable,
            // Figma Button(709:5452): 라이트 disabled는 흰색, 다크 disabled는 gray04
            disabledContentColor = if (isDarkTheme) feelinColors.gray04 else LightGray00,
        )
    ) {
        Text(
            text = text,
            style = FeelinTypography.title2,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeelinNormalButtonPreview() {
    FeelinTheme {
        FeelinNormalButton(
            text = "완료",
            enabled = true,
            onClick = {},
        )
    }
}

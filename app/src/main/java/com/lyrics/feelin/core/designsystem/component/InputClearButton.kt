package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.XCircleIcon

@Composable
fun InputClearButton(
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource?,
) {
    Icon(
        painter = XCircleIcon,
        contentDescription = "입력 내용 지우기",
        tint = color,
        modifier = modifier.size(20.dp).clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        )
    )
}

package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.XCircleIcon

/**
 * BasicTextField 기반의 입력창에서 텍스트필드에 공통된 디자인을 사용하기 위한 컴포넌트입니다.
 * */
@Composable
fun RowScope.InnerTextFieldComponent(
    isTextEmpty: Boolean,
    placeholder: String,
    placeholderTextStyle: TextStyle,
    placeholderColor: Color,
    clearIconColor: Color,
    innerTextField: @Composable (() -> Unit),
    onClearButtonClick: () -> Unit,
    clearButtonInteractionSource: MutableInteractionSource?,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.weight(1f)) {
        if (isTextEmpty) {
            Text(
                text = placeholder,
                style = placeholderTextStyle,
                color = placeholderColor,
            )
        }
        innerTextField()
    }

    // 클리어 버튼 (입력값 있을 때만 표시)
    if (!isTextEmpty) {
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = XCircleIcon,
            contentDescription = "입력 내용 지우기",
            tint = clearIconColor,
            modifier = Modifier.size(20.dp).clickable(
                interactionSource = clearButtonInteractionSource,
                indication = null,
                onClick = onClearButtonClick,
            )
        )
    }
}

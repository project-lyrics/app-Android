package com.lyrics.feelin.presentation.view.component.comment

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.InnerTextFieldComponent
import com.lyrics.feelin.core.designsystem.icon.SubmitArrowIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightGray00
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

private const val MAX_COMMENT_LENGTH = 255

@Composable
fun CommentInputField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String = "댓글을 입력하세요",
    onSendClick: (String) -> Unit = {},
) {
    val feelinColors = LocalFeelinColors.current
    val sendInteractionSource = remember { MutableInteractionSource() }
    val clearInteractionSource = remember { MutableInteractionSource() }
    val inputStatus = if (state.text.isEmpty()) CommentInputStatus.Waiting else CommentInputStatus.Writing
    val isSendEnabled = inputStatus == CommentInputStatus.Writing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            state = state,
            modifier = Modifier.weight(1f),
            lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5),
            inputTransformation = InputTransformation.maxLength(MAX_COMMENT_LENGTH),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Default,
                keyboardType = KeyboardType.Text,
                autoCorrectEnabled = false,
            ),
            textStyle = FeelinTypography.body3.copy(color = feelinColors.gray09),
            cursorBrush = SolidColor(feelinColors.gray09),
            decorator = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 44.dp)
                        .background(
                            color = feelinColors.inputField,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    InnerTextFieldComponent(
                        isTextEmpty = state.text.isEmpty(),
                        placeholder = placeholder,
                        placeholderTextStyle = FeelinTypography.body3,
                        placeholderColor = feelinColors.gray04,
                        clearIconColor = feelinColors.gray03,
                        innerTextField = innerTextField,
                        onClearButtonClick = { state.clearText() },
                        clearButtonInteractionSource = clearInteractionSource,
                        showClearButton = false,
                    )
                }
            },
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = if (isSendEnabled) feelinColors.systemActivate else feelinColors.systemDisable,
                    shape = CircleShape
                )
                .clickable(
                    enabled = isSendEnabled,
                    interactionSource = sendInteractionSource,
                    indication = null,
                    onClick = {
                        onSendClick(state.text.toString())
                        state.clearText()
                    },
                ),
        ) {
            Icon(
                imageVector = SubmitArrowIcon,
                contentDescription = "댓글 추가",
                // 피그마 디자인상 비활성화일때 LightGray00 사용
                tint = if (isSendEnabled) feelinColors.gray00 else LightGray00,
            )
        }
    }
}

@Preview(name = "Comment Input Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "Comment Input Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0C0C0D
)
@Composable
private fun CommentInputFieldPreview() {
    val state = remember { TextFieldState() }
    FeelinTheme {
        CommentInputField(state = state)
    }
}

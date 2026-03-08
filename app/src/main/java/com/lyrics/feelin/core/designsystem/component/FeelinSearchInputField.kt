package com.lyrics.feelin.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.lyrics.feelin.core.designsystem.icon.SearchIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

private const val MAX_INPUT_LENGTH: Int = 100

/**
 * Feelin 앱의 검색 입력 필드 컴포넌트입니다.
 *
 * 라이트/다크 모드에 따라 자동으로 색상이 변경됩니다.
 *
 * @param state 텍스트 입력 상태
 * @param modifier 컴포넌트 수정자
 * @param placeholder 입력 필드의 플레이스홀더 텍스트
 * @param onClearClick 클리어 버튼 클릭 시 호출되는 콜백
 */
@Composable
fun FeelinSearchInputField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onSearchClick: () -> Unit = {},
    onClearClick: () -> Unit = {}
) {
    val feelinColors = LocalFeelinColors.current

    val backgroundColor = feelinColors.inputField
    val textColor = feelinColors.gray09
    val placeholderColor = feelinColors.gray04
    val iconTintColor = feelinColors.gray05
    val clearButtonColor = feelinColors.gray03

    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        state = state,
        modifier = modifier,
        lineLimits = TextFieldLineLimits.SingleLine,
        inputTransformation = InputTransformation.maxLength(MAX_INPUT_LENGTH),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text,
            autoCorrectEnabled = false
        ),
        onKeyboardAction = { onSearchClick.invoke() },
        textStyle = FeelinTypography.body3.copy(color = textColor),
        cursorBrush = SolidColor(textColor),
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(all = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 검색 아이콘
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconTintColor,
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 텍스트 입력 영역
                InnerTextFieldComponent(
                    isTextEmpty = state.text.isEmpty(),
                    placeholder = placeholder,
                    placeholderColor = placeholderColor,
                    placeholderTextStyle = FeelinTypography.body3,
                    clearIconColor = clearButtonColor,
                    innerTextField = innerTextField,
                    onClearButtonClick = { onClearClick.invoke() },
                    clearButtonInteractionSource = interactionSource
                )
            }
        },
    )
}

@Preview(name = "Light mode", showBackground = true, backgroundColor = 0xff000000)
@Preview(name = "Dark mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelinSearchInputFieldPreview() {
    val state = remember { TextFieldState() }
    FeelinTheme {
        FeelinSearchInputField(
            state = state,
            placeholder = "텍스트",
            onSearchClick = { println("onSearchClick invoke") },
            onClearClick = { state.clearText() }
        )
    }
}

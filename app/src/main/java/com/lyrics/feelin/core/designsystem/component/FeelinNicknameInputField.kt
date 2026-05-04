package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

private const val MAX_INPUT_LENGTH: Int = 10

private val VALID_NICKNAME_REGEX = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]*$")

sealed class NicknameValidationResult {
    data object Valid : NicknameValidationResult()
    data object InvalidCharacter : NicknameValidationResult()
    data object TooLong : NicknameValidationResult()
}

fun validateNickname(text: CharSequence): NicknameValidationResult {
    return when {
        !text.toString().matches(VALID_NICKNAME_REGEX) -> NicknameValidationResult.InvalidCharacter
        text.length > MAX_INPUT_LENGTH -> NicknameValidationResult.TooLong
        else -> NicknameValidationResult.Valid
    }
}

@Composable
fun FeelinNicknameInputField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onClearClick: () -> Unit = {},
    isEnabled: Boolean = true
) {
    val feelinColors = LocalFeelinColors.current

    val textColor = if (isEnabled) feelinColors.gray08 else feelinColors.gray03
    val placeholderColor = feelinColors.gray03
    val clearButtonColor = feelinColors.gray03
    val errorColor = feelinColors.alertWarning

    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val validationResult = validateNickname(state.text)
    val isError = validationResult != NicknameValidationResult.Valid && state.text.isNotEmpty()
    val errorMessage = when (validationResult) {
        is NicknameValidationResult.InvalidCharacter -> "공백, 특수문자, 이모티콘은 사용 불가합니다"
        is NicknameValidationResult.TooLong -> "1~10자의 닉네임을 사용해주세요"
        else -> null
    }

    BasicTextField(
        state = state,
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
            autoCorrectEnabled = false
        ),
        onKeyboardAction = { keyboardController?.hide() },
        textStyle = FeelinTypography.title2.copy(color = textColor),
        cursorBrush = SolidColor(textColor),
        decorator = { innerTextField ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    InnerTextFieldComponent(
                        isTextEmpty = state.text.isEmpty(),
                        placeholder = placeholder,
                        placeholderColor = placeholderColor,
                        placeholderTextStyle = FeelinTypography.title2,
                        clearIconColor = clearButtonColor,
                        innerTextField = innerTextField,
                        onClearButtonClick = { onClearClick.invoke() },
                        clearButtonInteractionSource = interactionSource,
                        showClearButton = isEnabled
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(if (isError) errorColor else feelinColors.gray02)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isError && errorMessage != null) {
                        Text(
                            text = errorMessage,
                            style = FeelinTypography.body3,
                            color = errorColor,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "${state.text.length}/$MAX_INPUT_LENGTH",
                        style = FeelinTypography.body3,
                        color = if (isError) errorColor else feelinColors.gray02,
                    )
                }
            }
        }
    )
}

@Preview(name = "기본 상태")
@Composable
private fun FeelinNicknameInputFieldPreview() {
    FeelinTheme {
        FeelinNicknameInputField(
            state = TextFieldState(initialText = "실리카겔짱"),
            placeholder = "닉네임"
        )
    }
}

@Preview(name = "비활성화 상태")
@Composable
private fun FeelinNicknameInvalidPreview() {
    FeelinTheme {
        FeelinNicknameInputField(

            state = TextFieldState(initialText = ""),
            placeholder = "닉네임"
        )
    }
}

@Preview(name = "특수문자 에러")
@Composable
private fun FeelinNicknameInputFieldInvalidCharPreview() {
    FeelinTheme {
        FeelinNicknameInputField(
            state = TextFieldState(initialText = "#\$%%^%&&"),
            placeholder = "닉네임"
        )
    }
}

@Preview(name = "글자수 초과 에러")
@Composable
private fun FeelinNicknameInputFieldTooLongPreview() {
    FeelinTheme {
        FeelinNicknameInputField(
            state = TextFieldState(initialText = "가나다라마바사아자차카"),
            placeholder = "닉네임"
        )
    }
}

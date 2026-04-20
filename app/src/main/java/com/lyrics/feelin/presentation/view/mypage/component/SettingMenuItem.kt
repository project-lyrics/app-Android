package com.lyrics.feelin.presentation.view.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.CaretIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

private const val CARET_ROTATE_TO_RIGHT = 270f

/**
 * 우측 caret과 함께 다음 화면 이동을 나타내는 메뉴형 설정 항목입니다.
 */
@Composable
fun SettingMenuItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current

    MyPageItemRowShell(
        modifier = modifier.clickable(onClick = onClick),
        leadingContent = {
            Text(text = title, style = FeelinTypography.body1.copy(color = feelinColors.gray09))
        },
        trailingContent = {
            Icon(
                imageVector = CaretIcon,
                contentDescription = null,
                tint = feelinColors.gray05,
                modifier = Modifier
                    .size(18.dp)
                    .rotate(CARET_ROTATE_TO_RIGHT),
            )
        },
    )
}

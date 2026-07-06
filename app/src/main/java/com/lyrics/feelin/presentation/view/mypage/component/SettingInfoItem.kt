package com.lyrics.feelin.presentation.view.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

/**
 * 좌측 제목과 우측 보조 영역을 함께 보여주는 정보형 설정 항목입니다.
 */
@Composable
fun SettingInfoItem(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color? = null,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
) {
    val feelinColors = LocalFeelinColors.current
    val rowModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    MyPageItemRowShell(
        modifier = rowModifier,
        leadingContent = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = FeelinTypography.body1.copy(color = titleColor ?: feelinColors.gray09),
            )
        },
        trailingContent = trailingContent,
    )
}

/**
 * 좌측 제목과 우측 설명 텍스트를 함께 보여주는 정보형 설정 항목입니다.
 */
@Composable
fun SettingInfoItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    titleColor: Color? = null,
) {
    val feelinColors = LocalFeelinColors.current

    SettingInfoItem(
        title = title,
        modifier = modifier,
        titleColor = titleColor,
        trailingContent = {
            Text(
                text = description,
                style = FeelinTypography.body2.copy(color = feelinColors.gray04),
            )
        },
    )
}

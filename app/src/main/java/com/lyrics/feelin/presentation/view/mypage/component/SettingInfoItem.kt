package com.lyrics.feelin.presentation.view.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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

/**
 * 정보형 설정 항목의 trailing 영역에 들어가는 소형 액션 버튼입니다.
 */
@Composable
fun SettingInfoActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current

    Box(
        modifier = modifier
            .height(28.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .background(color = feelinColors.brandSecondary)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = FeelinTypography.body2.copy(color = feelinColors.brandPrimary),
        )
    }
}

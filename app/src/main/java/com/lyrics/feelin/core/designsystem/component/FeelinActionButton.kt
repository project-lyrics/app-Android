package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

/**
 * 정보형 설정 항목의 trailing 영역에 들어가는 소형 액션 버튼에 기반한 컴포넌트입니다.
 * (Figma component name Button_Copy)
 */
@Composable
fun FeelinActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current

    Box(
        modifier = modifier
            .width(68.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(4.dp))
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

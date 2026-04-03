package com.lyrics.feelin.presentation.view.mypage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 마이페이지 항목들의 공통 행 레이아웃만 담당하는 내부 shell입니다.
 */
@Composable
internal fun MyPageItemRowShell(
    leadingContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent()
        trailingContent?.invoke(this)
    }
}

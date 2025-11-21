package com.lyrics.feelin.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun FeelinTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit
) {
    val feelinColors = LocalFeelinColors.current

    Tab(
        selected = selected,
        onClick = onClick::invoke,
        enabled = enabled,
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = feelinColors.gray03,
        text = text
    )
}

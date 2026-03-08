package com.lyrics.feelin.core.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeelinTabRow(selectedTabIndex: Int, modifier: Modifier = Modifier, tabs: @Composable (() -> Unit)) {
    val feelinColors = LocalFeelinColors.current

    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = feelinColors.backgroundPrimary,
        divider = @Composable {
            HorizontalDivider(
                thickness = 4.dp,
                color = feelinColors.gray01
            )
        },
        tabs = tabs
    )
}

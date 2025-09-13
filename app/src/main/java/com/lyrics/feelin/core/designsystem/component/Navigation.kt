package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.HomeActiveIcon
import com.lyrics.feelin.core.designsystem.icon.HomeInactiveIcon
import com.lyrics.feelin.core.designsystem.icon.MyPageActiveIcon
import com.lyrics.feelin.core.designsystem.icon.MyPageInactiveIcon
import com.lyrics.feelin.core.designsystem.icon.NoteSearchingActiveIcon
import com.lyrics.feelin.core.designsystem.icon.NoteSearchingInactiveIcon
import com.lyrics.feelin.presentation.designsystem.theme.DarkGray09
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LightGray08
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

data class BottomNavItem(
    val inactiveIcon: @Composable () -> ImageVector,
    val activeIcon: @Composable () -> ImageVector
)

@Composable
fun FeelinBottomNavigation(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                drawLine(
                    color = feelinColors.gray01,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 2f
                )
            },
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                FeelinBottomNavItem(
                    item = item,
                    isSelected = selectedIndex == index,
                    onClick = { onItemSelected(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FeelinBottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) item.activeIcon() else item.inactiveIcon(),
                tint = if (isSystemInDarkTheme()) DarkGray09 else LightGray08,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(name = "Bottom Navigation - Light", showBackground = true)
@Composable
private fun FeelinNavigationRailPreview() {
    val items = listOf(
        BottomNavItem(
            activeIcon = { HomeActiveIcon },
            inactiveIcon = { HomeInactiveIcon }
        ),
        BottomNavItem(
            activeIcon = { NoteSearchingActiveIcon },
            inactiveIcon = { NoteSearchingInactiveIcon }
        ),
        BottomNavItem(
            activeIcon = { MyPageActiveIcon },
            inactiveIcon = { MyPageInactiveIcon }
        )
    )

    FeelinTheme {
        FeelinBottomNavigation(
            items = items,
            selectedIndex = 0,
            onItemSelected = {}
        )
    }
}

@Preview(name = "Bottom Navigation - Dark", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelinNavigationRailDarkPreview() {
    val items = listOf(
        BottomNavItem(
            activeIcon = { HomeActiveIcon },
            inactiveIcon = { HomeInactiveIcon }
        ),
        BottomNavItem(
            activeIcon = { NoteSearchingActiveIcon },
            inactiveIcon = { NoteSearchingInactiveIcon }
        ),
        BottomNavItem(
            activeIcon = { MyPageActiveIcon },
            inactiveIcon = { MyPageInactiveIcon }
        )
    )

    FeelinTheme {
        FeelinBottomNavigation(
            items = items,
            selectedIndex = 0,
            onItemSelected = {}
        )
    }
}

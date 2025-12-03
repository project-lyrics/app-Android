@file:Suppress("UnusedParameter")
// TODO(@이대근): 추후 앱바 수정하면서도 파라미터 불필요하면 삭제하겠습니다 2025.09.16.

package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.BackIcon
import com.lyrics.feelin.core.designsystem.icon.NotificationIcon
import com.lyrics.feelin.core.designsystem.icon.SettingsIconDark
import com.lyrics.feelin.core.designsystem.icon.SettingsIconLight
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightGray09
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun FeelinTransparentTopAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: () -> Unit = {},
    colors: FeelinTopAppBarColors = FeelinTopAppBarDefaults.topAppBarColors(),
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(
        horizontal = FeelinTopAppBarDefaults.HorizontalPadding,
        vertical = FeelinTopAppBarDefaults.VerticalPadding,
    ),
) {
    TopAppBarBase(
        title = { Text(text = "") },
        actions = actions,
        colors = FeelinTopAppBarColors(
            containerColor = Color.Transparent,
            titleColor = colors.titleColor,
            actionIconColor = colors.actionIconColor,
            navigationIconColor = colors.navigationIconColor,
        ),
        centeredTitle = false,
        paddingValues = paddingValues,
        navigationIcon = {
            TopBarIconButton(
                imageVector = BackIcon,
                onClick = onBackClick,
                contentDescription = "Back",
                tint = LightGray09, // 투명 상태일 때는 다크모드 미 적용입니다.
            )
        },
        modifier = modifier,
    )
}

@Suppress("ModifierNotUsedAtRoot")
@Composable
fun FeelinTopAppBarWithBack(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: FeelinTopAppBarColors = FeelinTopAppBarDefaults.topAppBarColors(),
    actions: @Composable RowScope.() -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(
        horizontal = FeelinTopAppBarDefaults.HorizontalPadding,
        vertical = FeelinTopAppBarDefaults.VerticalPadding,
    ),
    centeredTitle: Boolean = false,
    showDivider: Boolean = true,
) {
    val feelinColors = LocalFeelinColors.current

    Column {
        TopAppBarBase(
            title = {
                Text(
                    text = title,
                    style = FeelinTypography.heading3.copy(color = feelinColors.gray09),
                )
            },
            actions = actions,
            colors = colors,
            centeredTitle = centeredTitle,
            paddingValues = paddingValues,
            navigationIcon = {
                TopBarIconButton(
                    imageVector = BackIcon,
                    onClick = onBackClick,
                    contentDescription = "Back",
                    tint = feelinColors.gray09,
                )
            },
            modifier = modifier,
        )
        if (showDivider) {
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .size(1.dp)
                    .background(feelinColors.gray01)
            )
        }
    }
}

@Suppress("ModifierNotUsedAtRoot")
@Composable
fun FeelinTopAppBarNoBack(
    title: String,
    modifier: Modifier = Modifier,
    colors: FeelinTopAppBarColors = FeelinTopAppBarDefaults.topAppBarColors(),
    actions: @Composable RowScope.() -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(
        horizontal = FeelinTopAppBarDefaults.HorizontalPadding,
        vertical = FeelinTopAppBarDefaults.VerticalPadding,
    ),
    centeredTitle: Boolean = false,
    showDivider: Boolean = false,
) {
    val feelinColors = LocalFeelinColors.current

    Column {
        TopAppBarBase(
            title = {
                Text(
                    text = title,
                    style = FeelinTypography.heading3.copy(color = feelinColors.gray09),
                )
            },
            actions = actions,
            colors = colors,
            centeredTitle = centeredTitle,
            paddingValues = paddingValues,
            navigationIcon = null,
            modifier = modifier,
        )
        if (showDivider) {
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .size(1.dp)
                    .background(feelinColors.gray01)
            )
        }
    }
}

@Suppress("ModifierWithoutDefault")
@Composable
private fun TopAppBarBase(
    title: @Composable (() -> Unit),
    colors: FeelinTopAppBarColors,
    navigationIcon: @Composable (() -> Unit)?,
    actions: @Composable (RowScope.() -> Unit),
    paddingValues: PaddingValues,
    centeredTitle: Boolean,
    modifier: Modifier,
) {
    val actionsRow = @Composable {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = FeelinTopAppBarDefaults.ActionsSpacing,
            ),
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.containerColor)
            .padding(paddingValues),
    ) {
        if (navigationIcon != null) {
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                CompositionLocalProvider(content = navigationIcon)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 0.dp)
        ) {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = 1f,
                ),
                content = title
            )
        }

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            CompositionLocalProvider(content = actionsRow)
        }
    }
}

@Composable
fun TopBarIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Box(
        modifier = modifier
            .size(FeelinTopAppBarDefaults.ActionIconSize)
            .clickable(
                enabled = true,
                indication = null,
                interactionSource = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            tint = tint,
        )
    }
}

@Preview(name = "Transparent TopBar - Light", showBackground = true)
@Preview(
    name = "Transparent TopBar - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FeelinTransparentTopAppBarPreview() {
    FeelinTheme {
        Box {
            FeelinTransparentTopAppBar(
                onBackClick = {},
                actions = {
                    TopBarIconButton(
                        imageVector = NotificationIcon,
                        contentDescription = "알림",
                        onClick = {}
                    )
                }
            )
        }
    }
}

@Preview(name = "TopBar with Back - Light", showBackground = true)
@Preview(
    name = "TopBar with Back - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FeelinTopAppBarWithBackPreview() {
    FeelinTheme {
        Box {
            FeelinTopAppBarWithBack(
                title = "필릭스 레코드",
                onBackClick = {}
            )
        }
    }
}

@Preview(
    name = "TopBar no back with double icon - Light",
    showBackground = true
)
@Preview(
    name = "TopBar no back with double icon - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FeelinTopAppBarDoubleIconPreview() {
    FeelinTheme {
        Box {
            FeelinTopAppBarNoBack(
                title = "마이페이지",
                actions = {
                    TopBarIconButton(
                        imageVector = if (isSystemInDarkTheme()) SettingsIconDark else SettingsIconLight,
                        contentDescription = "설정",
                        tint = LocalFeelinColors.current.gray09,
                        onClick = {}
                    )
                    TopBarIconButton(
                        imageVector = NotificationIcon,
                        contentDescription = "알림",
                        tint = LocalFeelinColors.current.gray09,
                        onClick = {}
                    )
                }
            )
        }
    }
}

object FeelinTopAppBarDefaults {

    val HorizontalPadding: Dp = 20.dp
    val VerticalPadding: Dp = 16.dp
    val ActionIconSize: Dp = 24.dp
    val ActionButtonSize: Dp = 24.dp
    val NavigationButtonSize: Dp = 24.dp
    val ActionsSpacing: Dp = 20.dp

    val ActionIconColor: Color = LightGray09
    val NavigationIconColor: Color = LightGray09

    @Composable
    fun topAppBarColors(
        containerColor: Color = LocalFeelinColors.current.gray00,
        titleContentColor: Color = LocalFeelinColors.current.gray09,
        actionIconContentColor: Color = ActionIconColor,
        navigationIconContentColor: Color = NavigationIconColor
    ): FeelinTopAppBarColors = FeelinTopAppBarColors(
        containerColor = containerColor,
        titleColor = titleContentColor,
        actionIconColor = actionIconContentColor,
        navigationIconColor = navigationIconContentColor,
    )
}

data class FeelinTopAppBarColors(
    val containerColor: Color,
    val titleColor: Color,
    val navigationIconColor: Color,
    val actionIconColor: Color
)

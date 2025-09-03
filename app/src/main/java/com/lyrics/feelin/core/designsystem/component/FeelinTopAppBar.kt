package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyrics.feelin.core.designsystem.icon.BackIcon
import com.lyrics.feelin.core.designsystem.icon.NotificationIcon
import com.lyrics.feelin.presentation.designsystem.theme.LightGray01
import com.lyrics.feelin.presentation.designsystem.theme.LightGray09

@Composable
fun FeelinTransparentTopAppBar(
    title: () -> Unit = {},
    modifier: Modifier = Modifier,
    colors: FeelinTopAppBarColors = FeelinTopAppBarDefaults.topAppBarColors(),
    navigationIcon: (@Composable () -> Unit)? = null,
    onBackClick: () -> Unit,
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
            )
        },
        modifier = modifier,
    )
}

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
) {
    TopAppBarBase(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(700),
                    color = LightGray09,
                    textAlign = TextAlign.Center,
                )
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
            )
        },
        modifier = modifier,
    )
    Spacer(
        Modifier
            .size(1.dp)
            .background(LightGray01)
    )
}

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
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                CompositionLocalProvider(
                    content = navigationIcon
                )
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

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            CompositionLocalProvider(
                content = actionsRow
            )
        }
    }
}


@Composable
fun TopBarIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String,
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
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeelinTransparentTopAppBarPreview() {
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

@Preview(showBackground = true)
@Composable
private fun FeelinTopAppBarWithBackPreview() {
    Box {
        FeelinTopAppBarWithBack(
            title = "필릭스 레코드",
            onBackClick = {}
        )
    }
}

object FeelinTopAppBarDefaults {

    val HorizontalPadding: Dp = 20.dp
    val VerticalPadding: Dp = 10.dp
    val ActionIconSize: Dp = 24.dp
    val ActionButtonSize: Dp = 24.dp
    val NavigationButtonSize: Dp = 24.dp
    val ActionsSpacing: Dp = 4.dp

    val TitleContentColor: Color = Black
    val ContainerColor: Color = White
    val ActionIconColor: Color = Black
    val NavigationIconColor: Color = Black

    @Composable
    fun topAppBarColors(
        containerColor: Color = ContainerColor,
        titleContentColor: Color = TitleContentColor,
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
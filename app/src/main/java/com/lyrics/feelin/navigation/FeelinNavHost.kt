package com.lyrics.feelin.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lyrics.feelin.core.designsystem.component.BottomNavItem
import com.lyrics.feelin.core.designsystem.component.FeelinBottomNavigation
import com.lyrics.feelin.core.designsystem.icon.HomeActiveIcon
import com.lyrics.feelin.core.designsystem.icon.HomeInactiveIcon
import com.lyrics.feelin.core.designsystem.icon.MyPageActiveIcon
import com.lyrics.feelin.core.designsystem.icon.MyPageInactiveIcon
import com.lyrics.feelin.core.designsystem.icon.NoteSearchingActiveIcon
import com.lyrics.feelin.core.designsystem.icon.NoteSearchingInactiveIcon
import com.lyrics.feelin.presentation.view.community.CommunityMainScreen
import com.lyrics.feelin.presentation.view.mypage.MyPageScreen
import com.lyrics.feelin.presentation.view.note.NoteSearchScreen

@Composable
fun FeelinNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = FeelinDestination.Home.route,
) {
    var selectedBottomBarIndex by remember { mutableIntStateOf(0) }

    val bottomBarItems = remember {
        listOf(
            BottomNavItem(
                inactiveIcon = { HomeInactiveIcon },
                activeIcon = { HomeActiveIcon }
            ),
            BottomNavItem(
                inactiveIcon = { NoteSearchingInactiveIcon },
                activeIcon = { NoteSearchingActiveIcon }
            ),
            BottomNavItem(
                inactiveIcon = { MyPageInactiveIcon },
                activeIcon = { MyPageActiveIcon }
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            FeelinBottomNavigation(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                items = bottomBarItems,
                selectedIndex = selectedBottomBarIndex,
                onItemSelected = { index ->
                    selectedBottomBarIndex = index
                    val destination = when (index) {
                        0 -> FeelinDestination.Home.route
                        1 -> FeelinDestination.NoteSearch.route
                        2 -> FeelinDestination.MyPage.route
                        else -> FeelinDestination.Home.route
                    }
                    navController.navigate(destination) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
        ) {
            composable(FeelinDestination.Home.route) {
                selectedBottomBarIndex = 0
                CommunityMainScreen("필릭스", onBack = {})
            }

            composable(FeelinDestination.NoteSearch.route) {
                selectedBottomBarIndex = 1
                NoteSearchScreen()
            }

            composable(FeelinDestination.MyPage.route) {
                selectedBottomBarIndex = 2
                MyPageScreen()
            }
        }
    }
}
package com.lyrics.feelin.navigation

import android.util.Log
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
import androidx.compose.ui.unit.dp
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
import com.lyrics.feelin.presentation.view.login.LoginScreen
import com.lyrics.feelin.presentation.view.mypage.MyPageScreen
import com.lyrics.feelin.presentation.view.note.NoteSearchScreen

@Composable
fun FeelinNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = FeelinDestination.Login.route,
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
            if (selectedBottomBarIndex != -1) {
                FeelinBottomNavigation(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                    items = bottomBarItems,
                    selectedIndex = selectedBottomBarIndex,
                    onItemSelect = { index ->
                        selectedBottomBarIndex = index
                        val destination =
                            when (index) {
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
                    },
                )
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        var bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        // resources.getIdentifier()를 이용해 내비게이션 바의 크기를 받는 방법도 시도해 보았지만,
        // 일부 경우에서는 크기를 제대로 가져오지 못합니다.
        // 이에 아래의 측정치를 따라, 받아온 bottomPadding의 dp가 30 이하일 경우 WindowInsets이 아닌
        // Scaffold에서 받은 bottomPadding을 하위 컴포저블에서 가지도록 수정합니다. @이대근
        // 픽셀9 에뮬레이터 24dp, S23울트라 실기기 14.857142.dp, 노트10플러스 실기기 15.142858.dp
        if (bottomPadding <= 30.dp) {
            bottomPadding = paddingValues.calculateBottomPadding()
        }
        Log.d("MainActivity", "FeelinNavHost: bottomPadding $bottomPadding")
        Log.d("MainActivity", "FeelinNavHost: paddingValues $paddingValues")

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = bottomPadding
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

            composable(FeelinDestination.Login.route) {
                selectedBottomBarIndex = -1
                LoginScreen()
            }
        }
    }
}

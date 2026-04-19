package com.lyrics.feelin.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
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
import com.lyrics.feelin.presentation.view.mypage.setting.SettingScreen
import com.lyrics.feelin.presentation.view.mypage.userinfo.UserInfoScreen
import com.lyrics.feelin.presentation.view.note.NoteSearchScreen
import com.lyrics.feelin.presentation.view.onboarding.OnboardingViewModel
import com.lyrics.feelin.presentation.view.onboarding.genderage.OnboardingGenderAgeScreen
import com.lyrics.feelin.presentation.view.onboarding.profile.ProfileScreen
import com.lyrics.feelin.presentation.view.onboarding.terms.OnboardingTermsScreen
import com.lyrics.feelin.presentation.view.onboarding.welcome.WelcomeScreen

@Composable
fun FeelinNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = FeelinDestination.OnboardingGraph.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize()
    ) {
        onboardingNavGraph(navController)

        mainNavGraph(navController)
    }
}

private fun NavGraphBuilder.onboardingNavGraph(navController: NavHostController) {
    navigation(
        startDestination = FeelinDestination.Login.route,
        route = FeelinDestination.OnboardingGraph.route
    ) {
        loginScreen(navController)
        onboardingTermsScreen(navController)
        onboardingGenderAgeScreen(navController)
        onboardingProfileScreen(navController)
        onboardingWelcomeScreen(navController)
    }
}

private fun NavGraphBuilder.loginScreen(navController: NavHostController) {
    composable(FeelinDestination.Login.route) {
        OnboardingScaffold {
            LoginScreen(
                onSignUp = {
                    navController.navigate(FeelinDestination.OnboardingTerms.route)
                },
                onContinueToMain = { navController.navigateToMainGraph() }
            )
        }
    }
}

private fun NavGraphBuilder.onboardingTermsScreen(navController: NavHostController) {
    composable(FeelinDestination.OnboardingTerms.route) {
        OnboardingScaffold {
            OnboardingTermsScreen(
                onBackClick = { navController.popBackStack() },
                onStartClick = { navController.navigate(FeelinDestination.OnboardingGenderAge.route) }
            )
        }
    }
}

private fun NavGraphBuilder.onboardingGenderAgeScreen(navController: NavHostController) {
    composable(FeelinDestination.OnboardingGenderAge.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(FeelinDestination.OnboardingGraph.route)
        }
        val viewModel: OnboardingViewModel = hiltViewModel(parentEntry)

        OnboardingScaffold {
            OnboardingGenderAgeScreen(
                onBackClick = { navController.popBackStack() },
                onSkipClick = { navController.navigate(FeelinDestination.OnboardingProfile.route) },
                onNextClick = { gender, birthYear ->
                    viewModel.saveGenderAndBirthYear(gender, birthYear)
                    navController.navigate(FeelinDestination.OnboardingProfile.route)
                }
            )
        }
    }
}

private fun NavGraphBuilder.onboardingProfileScreen(navController: NavHostController) {
    composable(FeelinDestination.OnboardingProfile.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(FeelinDestination.OnboardingGraph.route)
        }
        val viewModel: OnboardingViewModel = hiltViewModel(parentEntry)

        OnboardingScaffold {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onCompleteClick = { nickname, profileIndex ->
                    viewModel.saveProfile(nickname, profileIndex)
                    navController.navigate(FeelinDestination.OnboardingWelcome.route)
                }
            )
        }
    }
}

private fun NavGraphBuilder.onboardingWelcomeScreen(navController: NavHostController) {
    composable(FeelinDestination.OnboardingWelcome.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(FeelinDestination.OnboardingGraph.route)
        }
        val viewModel: OnboardingViewModel = hiltViewModel(parentEntry)

        OnboardingScaffold {
            WelcomeScreen(
                onNavigateToMain = {
                    viewModel.completeOnboarding()
                    navController.navigateToMainGraph()
                }
            )
        }
    }
}

private fun NavHostController.navigateToMainGraph() {
    navigate(FeelinDestination.MainGraph.route) {
        popUpTo(FeelinDestination.OnboardingGraph.route) { inclusive = true }
    }
}

private fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation(
        startDestination = FeelinDestination.HomeGraph.route,
        route = FeelinDestination.MainGraph.route
    ) {
        navigation(startDestination = FeelinDestination.Home.route, route = FeelinDestination.HomeGraph.route) {
            composable(FeelinDestination.Home.route) {
                MainScaffold(navController = navController, selectedIndex = 0) {
                    CommunityMainScreen("필릭스", onBack = {})
                }
            }
        }

        navigation(
            startDestination = FeelinDestination.NoteSearch.route,
            route = FeelinDestination.NoteSearchGraph.route
        ) {
            composable(FeelinDestination.NoteSearch.route) {
                MainScaffold(navController = navController, selectedIndex = 1) {
                    NoteSearchScreen()
                }
            }
        }

        navigation(startDestination = FeelinDestination.MyPage.route, route = FeelinDestination.MyPageGraph.route) {
            composable(FeelinDestination.MyPage.route) {
                MainScaffold(navController = navController, selectedIndex = 2) {
                    MyPageScreen(
                        onSettingClick = { navController.navigate(FeelinDestination.Setting.route) }
                    )
                }
            }

            composable(FeelinDestination.Setting.route) {
                MainScaffold(navController = navController, selectedIndex = 2) {
                    SettingScreen(
                        onBackClick = { navController.popBackStack() },
                        onUserInfoClick = { navController.navigate(FeelinDestination.UserInfo.route) }
                    )
                }
            }

            composable(FeelinDestination.UserInfo.route) {
                MainScaffold(navController = navController, selectedIndex = 2) {
                    UserInfoScreen(onBackClick = { navController.popBackStack() })
                }
            }
        }
    }
}

@Composable
private fun MainScaffold(
    navController: NavHostController,
    selectedIndex: Int,
    content: @Composable () -> Unit
) {
    var selectedBottomBarIndex by remember { mutableIntStateOf(selectedIndex) }

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
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            FeelinBottomNavigation(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                items = bottomBarItems,
                selectedIndex = selectedBottomBarIndex,
                onItemSelect = { index ->
                    selectedBottomBarIndex = index
                    val destination = when (index) {
                        0 -> FeelinDestination.HomeGraph.route
                        1 -> FeelinDestination.NoteSearchGraph.route
                        2 -> FeelinDestination.MyPageGraph.route
                        else -> FeelinDestination.HomeGraph.route
                    }
                    navController.navigate(destination) {
                        popUpTo(FeelinDestination.MainGraph.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        // WindowInsets.navigationBars가 30dp 이하인 경우 Scaffold paddingValues 사용
        var bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        if (bottomPadding <= 30.dp) {
            bottomPadding = paddingValues.calculateBottomPadding()
        }
        Log.d("FeelinNavHost", "MainScaffold: bottomPadding=$bottomPadding")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = bottomPadding
                )
        ) {
            content()
        }
    }
}

@Composable
private fun OnboardingScaffold(
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}

package com.lyrics.feelin.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.lyrics.feelin.core.designsystem.component.BottomNavItem
import com.lyrics.feelin.core.designsystem.component.FeelinBottomNavigation
import com.lyrics.feelin.core.designsystem.icon.HomeActiveIcon
import com.lyrics.feelin.core.designsystem.icon.HomeInactiveIcon
import com.lyrics.feelin.core.designsystem.icon.MyPageActiveIcon
import com.lyrics.feelin.core.designsystem.icon.MyPageInactiveIcon
import com.lyrics.feelin.core.designsystem.icon.NoteSearchingActiveIcon
import com.lyrics.feelin.core.designsystem.icon.NoteSearchingInactiveIcon
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.util.openExternalBrowser
import com.lyrics.feelin.presentation.view.community.CommunityMainScreen
import com.lyrics.feelin.presentation.view.login.LoginScreen
import com.lyrics.feelin.presentation.view.mypage.MyPageLogoutStatus
import com.lyrics.feelin.presentation.view.mypage.MyPageScreen
import com.lyrics.feelin.presentation.view.mypage.MyPageViewModel
import com.lyrics.feelin.presentation.view.mypage.setting.SettingScreen
import com.lyrics.feelin.presentation.view.mypage.userinfo.UserInfoScreen
import com.lyrics.feelin.presentation.view.note.search.NoteSearchScreen
import com.lyrics.feelin.presentation.view.note.search.result.NoteSearchResultScreen
import com.lyrics.feelin.presentation.view.onboarding.OnboardingUiState
import com.lyrics.feelin.presentation.view.onboarding.OnboardingViewModel
import com.lyrics.feelin.presentation.view.onboarding.genderage.OnboardingGenderAgeScreen
import com.lyrics.feelin.presentation.view.onboarding.profile.ProfileScreen
import com.lyrics.feelin.presentation.view.onboarding.terms.OnboardingTermsScreen
import com.lyrics.feelin.presentation.view.onboarding.welcome.WelcomeScreen
import com.lyrics.feelin.presentation.view.webview.InternalWebViewScreen

@Composable
fun FeelinNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = FeelinDestination.Splash.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize()
    ) {
        splashScreen(navController)

        onboardingNavGraph(navController)

        mainNavGraph(navController)

        internalWebViewScreen(navController)
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
    composable(FeelinDestination.Login.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(FeelinDestination.OnboardingGraph.route)
        }
        val viewModel: OnboardingViewModel = hiltViewModel(parentEntry)

        LoginScreenRoute(navController = navController, viewModel = viewModel)
    }

    composable(
        route = FeelinDestination.Login.ROUTE_WITH_AUTO_LOGIN_FAILED_ERROR_CODE,
        arguments = listOf(
            navArgument(FeelinDestination.Login.AUTO_LOGIN_FAILED_ERROR_CODE_ARGUMENT) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(FeelinDestination.OnboardingGraph.route)
        }
        val viewModel: OnboardingViewModel = hiltViewModel(parentEntry)
        val autoLoginFailedErrorCode = backStackEntry.arguments
            ?.getString(FeelinDestination.Login.AUTO_LOGIN_FAILED_ERROR_CODE_ARGUMENT)

        LoginScreenRoute(
            navController = navController,
            viewModel = viewModel,
            autoLoginFailedErrorCode = autoLoginFailedErrorCode,
        )
    }
}

@Composable
private fun LoginScreenRoute(
    navController: NavHostController,
    viewModel: OnboardingViewModel,
    autoLoginFailedErrorCode: String? = null,
) {
    OnboardingScaffold {
        LoginScreen(
            autoLoginFailedErrorCode = autoLoginFailedErrorCode,
            onSignUp = {
                viewModel.resetOnboardingState()
                navController.navigate(FeelinDestination.OnboardingTerms.route)
            },
            onContinueToMain = { navController.navigateToMainGraph() }
        )
    }
}

private fun NavGraphBuilder.onboardingTermsScreen(navController: NavHostController) {
    composable(FeelinDestination.OnboardingTerms.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(FeelinDestination.OnboardingGraph.route)
        }
        val viewModel: OnboardingViewModel = hiltViewModel(parentEntry)
        val onboardingState by viewModel.onboardingState.collectAsState()

        OnboardingScaffold {
            OnboardingTermsScreen(
                onBackClick = { navController.popBackStack() },
                onStartClick = { navController.navigate(FeelinDestination.OnboardingGenderAge.route) },
                termAgreements = onboardingState.termAgreements,
                onAllCheckedChange = viewModel::setAllTermsAgreed,
                onTermCheckedChange = viewModel::setTermAgreed,
                onDetailClick = { term ->
                    navController.navigate(FeelinDestination.InternalWebView.createRoute(term.webViewUrl))
                },
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
                onSkipClick = {
                    viewModel.clearGenderAndBirthYear()
                    navController.navigate(FeelinDestination.OnboardingProfile.route)
                },
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
        val onboardingUiState by viewModel.onboardingUiState.collectAsState()

        LaunchedEffect(onboardingUiState) {
            if (onboardingUiState is OnboardingUiState.SignUpSuccess) {
                viewModel.clearOnboardingUiState()
                navController.navigate(FeelinDestination.OnboardingWelcome.route) {
                    popUpTo(FeelinDestination.OnboardingProfile.route) { inclusive = true }
                }
            }
        }

        OnboardingScaffold {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onCompleteClick = { nickname, profileType ->
                    viewModel.signUp(nickname, profileType)
                },
                isLoading = onboardingUiState is OnboardingUiState.SigningUp,
                errorTitle = (onboardingUiState as? OnboardingUiState.Error)?.title,
                errorDescription = (onboardingUiState as? OnboardingUiState.Error)?.description,
                onErrorConfirmClick = {
                    val errorState = onboardingUiState as? OnboardingUiState.Error
                    viewModel.clearOnboardingUiState()

                    if (errorState?.kind == OnboardingUiState.Kind.MissingRequiredTerms) {
                        navController.navigate(FeelinDestination.OnboardingTerms.route) {
                            popUpTo(FeelinDestination.OnboardingTerms.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
    }
}

private fun NavGraphBuilder.onboardingWelcomeScreen(navController: NavHostController) {
    composable(FeelinDestination.OnboardingWelcome.route) {
        OnboardingScaffold {
            WelcomeScreen(
                onNavigateToMain = {
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
                    NoteSearchScreen(
                        onMusicClick = {
                            navController.navigate(FeelinDestination.NoteSearchResult.route)
                        }
                    )
                }
            }

            composable(FeelinDestination.NoteSearchResult.route) {
                MainScaffold(navController = navController, selectedIndex = 1) {
                    NoteSearchResultScreen(onBackClick = { navController.popBackStack() })
                }
            }
        }

        myPageNavGraph(navController)
    }
}

private fun NavGraphBuilder.myPageNavGraph(navController: NavHostController) {
    navigation(startDestination = FeelinDestination.MyPage.route, route = FeelinDestination.MyPageGraph.route) {
        composable(FeelinDestination.MyPage.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(FeelinDestination.MyPageGraph.route)
            }
            val viewModel: MyPageViewModel = hiltViewModel(parentEntry)

            MainScaffold(navController = navController, selectedIndex = 2) {
                MyPageScreen(
                    onSettingClick = { navController.navigate(FeelinDestination.Setting.route) },
                    viewModel = viewModel,
                )
            }
        }

        composable(FeelinDestination.Setting.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(FeelinDestination.MyPageGraph.route)
            }
            val viewModel: MyPageViewModel = hiltViewModel(parentEntry)
            val logoutStatus by viewModel.logoutStatus.collectAsState()
            val context = LocalContext.current

            LaunchedEffect(logoutStatus) {
                if (logoutStatus == MyPageLogoutStatus.SUCCESS) {
                    viewModel.clearLogoutStatus()
                    navController.navigate(FeelinDestination.OnboardingGraph.route) {
                        popUpTo(FeelinDestination.MainGraph.route) { inclusive = true }
                    }
                }
            }

            MainScaffold(
                navController = navController,
                selectedIndex = 2,
                isBlockingLoading = logoutStatus == MyPageLogoutStatus.LOADING
            ) {
                SettingScreen(
                    onBackClick = { navController.popBackStack() },
                    onUserInfoClick = { navController.navigate(FeelinDestination.UserInfo.route) },
                    onLogoutClick = viewModel::logout,
                    onInternalWebViewClick = { url ->
                        navController.navigate(FeelinDestination.InternalWebView.createRoute(url))
                    },
                    onExternalBrowserClick = { url ->
                        context.openExternalBrowser(url)
                    },
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

private fun NavGraphBuilder.internalWebViewScreen(navController: NavHostController) {
    composable(
        route = FeelinDestination.InternalWebView.route,
        arguments = listOf(
            navArgument(FeelinDestination.InternalWebView.UrlArgument) {
                type = NavType.StringType
                defaultValue = ""
            }
        ),
    ) { backStackEntry ->
        val url = backStackEntry.arguments
            ?.getString(FeelinDestination.InternalWebView.UrlArgument)
            .orEmpty()

        InternalWebViewScreen(
            url = url,
            onCloseClick = { navController.popBackStack() },
        )
    }
}

@Composable
private fun MainScaffold(
    navController: NavHostController,
    selectedIndex: Int,
    isBlockingLoading: Boolean = false,
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

    Box(modifier = Modifier.fillMaxSize()) {
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
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = bottomPadding
                    )
            ) {
                content()
            }
        }

        BackHandler(enabled = isBlockingLoading) { }

        if (isBlockingLoading) {
            val feelinColors = LocalFeelinColors.current

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(feelinColors.dim)
                    .clearAndSetSemantics { }
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                event.changes.forEach { pointerInputChange ->
                                    pointerInputChange.consume()
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
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

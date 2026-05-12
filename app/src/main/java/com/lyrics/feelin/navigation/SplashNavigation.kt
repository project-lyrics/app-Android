package com.lyrics.feelin.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.lyrics.feelin.presentation.view.splash.SplashScreen
import com.lyrics.feelin.presentation.view.splash.SplashUiState
import com.lyrics.feelin.presentation.view.splash.SplashViewModel

internal fun NavGraphBuilder.splashScreen(navController: NavHostController) {
    composable(FeelinDestination.Splash.route) {
        val viewModel: SplashViewModel = hiltViewModel()
        val splashUiState by viewModel.splashUiState.collectAsState()

        LaunchedEffect(splashUiState) {
            when (val currentState = splashUiState) {
                SplashUiState.CheckingSession -> Unit
                SplashUiState.NavigateToMain -> {
                    navController.navigate(FeelinDestination.MainGraph.route) {
                        popUpTo(FeelinDestination.Splash.route) { inclusive = true }
                    }
                }
                is SplashUiState.NavigateToLogin -> {
                    navController.navigate(currentState.loginRoute) {
                        popUpTo(FeelinDestination.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        SplashScreen()
    }
}

private val SplashUiState.NavigateToLogin.loginRoute: String
    get() = if (showAutoLoginFailedDialog) {
        FeelinDestination.Login.createRoute(
            FeelinDestination.Login.AutoLoginFailedDialogReason
        )
    } else {
        FeelinDestination.Login.route
    }

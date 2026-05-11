package com.lyrics.feelin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lyrics.feelin.navigation.FeelinNavHost
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.mainUiState.value is MainUiState.CheckingSession
        }
        enableEdgeToEdge()
        setContent {
            val mainUiState by mainViewModel.mainUiState.collectAsState()

            FeelinTheme {
                val readyState = mainUiState as? MainUiState.Ready
                if (readyState != null) {
                    FeelinNavHost(startDestination = readyState.startDestination)
                }
            }
        }
    }
}

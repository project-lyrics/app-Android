package com.lyrics.feelin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lyrics.feelin.navigation.FeelinNavHost
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeelinTheme {
                FeelinNavHost()
            }
        }
    }
}

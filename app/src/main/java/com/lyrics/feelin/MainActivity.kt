package com.lyrics.feelin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.view.community.CommunityView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeelinTheme {
                // MARK(@gdaegeun539): 여러 화면을 만든 이후 NavHost를 세팅하도록 합니다. 2025.08.16.
                CommunityView()
            }
        }
    }
}

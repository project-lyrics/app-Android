package com.lyrics.feelin.presentation.view.webview

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithClose
import com.lyrics.feelin.core.designsystem.component.TopBarIconButton
import com.lyrics.feelin.core.designsystem.icon.RefreshIcon
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

private const val WEB_VIEW_TITLE = "notion.so"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun InternalWebViewScreen(
    url: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current
    val isDarkTheme = isSystemInDarkTheme()
    var webView by remember { mutableStateOf<WebView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            webView?.stopLoading()
            webView?.destroy()
            webView = null
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .background(color = feelinColors.backgroundPrimary),
        containerColor = feelinColors.backgroundPrimary,
        topBar = {
            FeelinTopAppBarWithClose(
                title = WEB_VIEW_TITLE,
                onCloseClick = onCloseClick,
                actions = {
                    TopBarIconButton(
                        imageVector = RefreshIcon,
                        onClick = { webView?.reload() },
                        contentDescription = "Refresh",
                        tint = feelinColors.gray09,
                    )
                },
                showDivider = false,
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webView = this
                        // Notion의 full-height 레이아웃이 Android WebView에서 0px로 접히지 않도록 명시한다.
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                        settings.allowFileAccess = false
                        settings.allowContentAccess = false
                        applyDarkTheme(isDarkTheme = isDarkTheme)
                        loadUrl(url)
                    }
                },
                update = { view ->
                    view.applyDarkTheme(isDarkTheme = isDarkTheme)
                    if (view.url != url) {
                        view.loadUrl(url)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = feelinColors.backgroundPrimary),
            )
        }
    }
}

// TODO(@이대근): 웹뷰 내부에서 다크모드를 전환 가능하게 하려면 뷰시스템 테마 설정(themes.xml)에 다크모드를 활성화해야함. 2026.05.05.
@Suppress("DEPRECATION") // MARK(@이대근): 구버전 시스템의 웹뷰 다크모드 설정을 위함 2026.05.05.
private fun WebView.applyDarkTheme(isDarkTheme: Boolean) {
    if (WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
        WebSettingsCompat.setAlgorithmicDarkeningAllowed(settings, isDarkTheme)
    } else if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
        WebSettingsCompat.setForceDark(
            settings,
            if (isDarkTheme) {
                WebSettingsCompat.FORCE_DARK_ON
            } else {
                WebSettingsCompat.FORCE_DARK_OFF
            },
        )
    }
}

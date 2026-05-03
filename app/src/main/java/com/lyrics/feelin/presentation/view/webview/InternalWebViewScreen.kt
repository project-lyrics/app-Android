package com.lyrics.feelin.presentation.view.webview

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
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
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                        settings.allowFileAccess = false
                        settings.allowContentAccess = false
                        loadUrl(url)
                    }
                },
                update = { view ->
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

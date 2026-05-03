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
private const val WEB_VIEW_NOTION_LAYOUT_PATCH_SCRIPT = """
    (function() {
        var styleId = 'feelin-notion-layout-patch';
        var css = [
            'html, body {',
            '  height: auto !important;',
            '  min-height: 100% !important;',
            '  overflow-y: auto !important;',
            '}',
            '.notion-frame, .notion-scroller {',
            '  height: auto !important;',
            '  min-height: 100vh !important;',
            '}',
            '.notion-scroller {',
            '  overflow-y: auto !important;',
            '}',
            '.notion-page-content {',
            '  min-height: 100vh !important;',
            '}'
        ].join('\n');

        function upsertStyle() {
            var style = document.getElementById(styleId);
            if (!style) {
                style = document.createElement('style');
                style.id = styleId;
                document.head.appendChild(style);
            }
            if (style.textContent !== css) {
                style.textContent = css;
            }
        }

        function patchElement(selector) {
            var element = document.querySelector(selector);
            if (!element) {
                return false;
            }
            setImportantStyle(element, 'height', 'auto');
            setImportantStyle(element, 'min-height', '100vh');
            return true;
        }

        function setImportantStyle(element, name, value) {
            if (
                element.style.getPropertyValue(name) !== value ||
                element.style.getPropertyPriority(name) !== 'important'
            ) {
                element.style.setProperty(name, value, 'important');
            }
        }

        function patchLayout() {
            upsertStyle();
            patchElement('.notion-frame');
            patchElement('.notion-scroller');
        }

        patchLayout();
        window.requestAnimationFrame(patchLayout);
        window.setTimeout(patchLayout, 500);
        window.setTimeout(patchLayout, 1000);

        if (!window.__feelinNotionLayoutObserver) {
            window.__feelinNotionLayoutPatchScheduled = false;
            window.__feelinNotionLayoutObserver = new MutationObserver(function() {
                if (window.__feelinNotionLayoutPatchScheduled) {
                    return;
                }
                window.__feelinNotionLayoutPatchScheduled = true;
                window.requestAnimationFrame(function() {
                    window.__feelinNotionLayoutPatchScheduled = false;
                    patchLayout();
                });
            });
            window.__feelinNotionLayoutObserver.observe(document.documentElement, {
                attributes: true,
                childList: true,
                subtree: true
            });
        }

        return 'patched';
    })();
"""

private fun WebView.patchNotionLayout() {
    evaluateJavascript(WEB_VIEW_NOTION_LAYOUT_PATCH_SCRIPT, null)
}

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
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                view?.patchNotionLayout()
                            }
                        }
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

package com.lyrics.feelin.presentation.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri

private const val EXTERNAL_BROWSER_LAUNCHER_TAG = "ExternalBrowserLauncher"

fun Context.openExternalBrowser(url: String) {
    runCatching {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }.onFailure { throwable ->
        if (throwable is ActivityNotFoundException) {
            Log.w(EXTERNAL_BROWSER_LAUNCHER_TAG, "No browser found for url: $url", throwable)
            Toast.makeText(this, "링크를 띄울 웹 브라우저를 설치해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            throw throwable
        }
    }
}

package com.niooon.browser.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.niooon.browser.ui.components.FloatingGlassDock
import com.niooon.browser.ui.theme.GoogleBlue

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    initialUrl: String,
    onCloseWebView: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var loadProgress by remember { mutableFloatStateOf(0f) }
    var isLoading by remember { mutableStateOf(true) }

    BackHandler(enabled = true) {
        if (webViewInstance?.canGoBack() == true) {
            webViewInstance?.goBack()
        } else {
            onCloseWebView()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isLoading && loadProgress < 1f) {
                LinearProgressIndicator(
                    progress = { loadProgress },
                    color = GoogleBlue,
                    trackColor = Color(0x334285F4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                )
            }

            AndroidView(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                return false
                            }

                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                canGoBack = view?.canGoBack() ?: false
                                canGoForward = view?.canGoForward() ?: false
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                loadProgress = newProgress / 100f
                                if (newProgress == 100) {
                                    isLoading = false
                                }
                            }
                        }

                        loadUrl(initialUrl)
                        webViewInstance = this
                    }
                },
                update = { webView ->
                    webViewInstance = webView
                }
            )

            // Space for the bottom dock
            Box(modifier = Modifier.height(84.dp).background(Color(0xFFE3F2FD)))
        }

        // Floating Glass Dock at Bottom
        FloatingGlassDock(
            currentTabCount = 1,
            canGoBack = canGoBack,
            canGoForward = canGoForward,
            onBackClick = {
                if (webViewInstance?.canGoBack() == true) {
                    webViewInstance?.goBack()
                } else {
                    onCloseWebView()
                }
            },
            onForwardClick = {
                if (webViewInstance?.canGoForward() == true) {
                    webViewInstance?.goForward()
                }
            },
            onSearchClick = onSearchClick,
            onTabsClick = onCloseWebView,
            onMenuClick = { webViewInstance?.reload() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

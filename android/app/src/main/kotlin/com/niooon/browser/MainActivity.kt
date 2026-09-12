package com.niooon.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.niooon.browser.ui.screens.BrowserHomeScreen
import com.niooon.browser.ui.screens.WebViewScreen
import com.niooon.browser.ui.theme.NiooonuBrowserTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NiooonuBrowserTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var activeUrl by remember { mutableStateOf<String?>(null) }

                    if (activeUrl != null) {
                        WebViewScreen(
                            initialUrl = activeUrl!!,
                            onCloseWebView = { activeUrl = null },
                            onSearchClick = { activeUrl = null }
                        )
                    } else {
                        BrowserHomeScreen(
                            onNavigateToUrl = { url ->
                                activeUrl = url
                            }
                        )
                    }
                }
            }
        }
    }
}

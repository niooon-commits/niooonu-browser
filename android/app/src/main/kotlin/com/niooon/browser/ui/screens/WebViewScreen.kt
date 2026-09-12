package com.niooon.browser.ui.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.niooon.browser.ui.components.BrowserPopupMenu
import com.niooon.browser.ui.components.InBrowserTopBar
import com.niooon.browser.ui.theme.GoogleBlue
import com.niooon.browser.ui.theme.TextPrimary
import com.niooon.browser.ui.theme.TextSecondary
import java.net.URLEncoder

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    initialUrl: String,
    tabCount: Int = 1,
    onHomeClick: () -> Unit,
    onNewTabClick: () -> Unit,
    onTabsClick: () -> Unit,
    onCloseWebView: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var currentUrl by remember { mutableStateOf(initialUrl) }
    var currentTitle by remember { mutableStateOf("") }
    var loadProgress by remember { mutableFloatStateOf(0f) }
    var isLoading by remember { mutableStateOf(true) }
    var isDesktopSite by remember { mutableStateOf(false) }

    // Dialog states
    var showUrlEditDialog by remember { mutableStateOf(false) }
    var editableUrlText by remember { mutableStateOf(initialUrl) }
    var showMenuDialog by remember { mutableStateOf(false) }

    // Update URL if initialUrl changes from external navigation
    LaunchedEffect(initialUrl) {
        if (initialUrl != currentUrl) {
            currentUrl = initialUrl
            webViewInstance?.loadUrl(initialUrl)
        }
    }

    BackHandler(enabled = true) {
        if (webViewInstance?.canGoBack() == true) {
            webViewInstance?.goBack()
        } else {
            onHomeClick()
        }
    }

    fun navigateTo(raw: String) {
        val trimmed = raw.trim()
        if (trimmed.isEmpty()) return
        val target = if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            trimmed
        } else if (trimmed.contains(".") && !trimmed.contains(" ")) {
            "https://$trimmed"
        } else {
            "https://www.google.com/search?q=" + URLEncoder.encode(trimmed, "UTF-8")
        }
        currentUrl = target
        webViewInstance?.loadUrl(target)
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF0F172A))) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. In-Browser Top Bar (Home, Pill URL, New Tab +, Tabs Badge [N], 3-Dots Menu)
            InBrowserTopBar(
                currentUrl = currentUrl,
                tabCount = tabCount,
                onHomeClick = onHomeClick,
                onUrlClick = {
                    editableUrlText = currentUrl
                    showUrlEditDialog = true
                },
                onNewTabClick = onNewTabClick,
                onTabsClick = onTabsClick,
                onMenuClick = { showMenuDialog = true }
            )

            // 2. Loading progress bar
            if (isLoading && loadProgress < 1f) {
                LinearProgressIndicator(
                    progress = { loadProgress },
                    color = GoogleBlue,
                    trackColor = Color(0x224285F4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(0.dp))
            }

            // 3. Web content takes 100% of remaining screen (NO BOTTOM NAVIGATION BAR!)
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
                                url?.let { currentUrl = it }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                url?.let { currentUrl = it }
                                view?.title?.let { currentTitle = it }
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                loadProgress = newProgress / 100f
                                if (newProgress == 100) {
                                    isLoading = false
                                }
                            }

                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                super.onReceivedTitle(view, title)
                                title?.let { currentTitle = it }
                            }
                        }

                        loadUrl(currentUrl)
                        webViewInstance = this
                    }
                },
                update = { webView ->
                    webViewInstance = webView
                }
            )
        }

        // Dialog: Edit URL / New Search
        if (showUrlEditDialog) {
            AlertDialog(
                onDismissRequest = { showUrlEditDialog = false },
                title = {
                    Text("Search or Enter URL", fontWeight = FontWeight.Bold, color = TextPrimary)
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = editableUrlText,
                            onValueChange = { editableUrlText = it },
                            placeholder = { Text("https://example.com or query") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoogleBlue,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showUrlEditDialog = false
                            navigateTo(editableUrlText)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoogleBlue)
                    ) {
                        Text("Go")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUrlEditDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }

        // 3-Dots Menu Popup (Matches Chrome Android dark mode popup with circular actions and clean vector icons)
        if (showMenuDialog) {
            BrowserPopupMenu(
                isDesktopMode = isDesktopSite,
                onDismiss = { showMenuDialog = false },
                onBack = {
                    if (webViewInstance?.canGoBack() == true) {
                        webViewInstance?.goBack()
                    } else {
                        onHomeClick()
                    }
                },
                onForward = {
                    if (webViewInstance?.canGoForward() == true) {
                        webViewInstance?.goForward()
                    } else {
                        Toast.makeText(context, "No forward page", Toast.LENGTH_SHORT).show()
                    }
                },
                onBookmark = {
                    Toast.makeText(context, "Page bookmarked", Toast.LENGTH_SHORT).show()
                },
                onDownload = {
                    Toast.makeText(context, "Downloading page for offline viewing...", Toast.LENGTH_SHORT).show()
                },
                onReload = {
                    webViewInstance?.reload()
                },
                onNewTab = onNewTabClick,
                onNewIncognitoTab = {
                    onNewTabClick()
                    Toast.makeText(context, "Incognito tab opened", Toast.LENGTH_SHORT).show()
                },
                onMoveTabToGroup = {
                    Toast.makeText(context, "Tab moved to group", Toast.LENGTH_SHORT).show()
                },
                onManageWindows = {
                    onTabsClick()
                },
                onHistory = {
                    Toast.makeText(context, "History opened", Toast.LENGTH_SHORT).show()
                },
                onDeleteBrowsingData = {
                    webViewInstance?.clearCache(true)
                    webViewInstance?.clearHistory()
                    Toast.makeText(context, "Browsing data and cache cleared", Toast.LENGTH_SHORT).show()
                },
                onSiteControls = {
                    Toast.makeText(context, "Site permissions and controls", Toast.LENGTH_SHORT).show()
                },
                onDownloadsList = {
                    Toast.makeText(context, "Downloads manager", Toast.LENGTH_SHORT).show()
                },
                onBookmarksList = {
                    Toast.makeText(context, "Bookmarks manager", Toast.LENGTH_SHORT).show()
                },
                onRecentTabs = {
                    onTabsClick()
                },
                onShare = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, currentUrl)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share link")
                    context.startActivity(shareIntent)
                },
                onFindInPage = {
                    Toast.makeText(context, "Find in page activated", Toast.LENGTH_SHORT).show()
                },
                onTranslate = {
                    val translateUrl = "https://translate.google.com/translate?sl=auto&tl=en&u=" + URLEncoder.encode(currentUrl, "UTF-8")
                    navigateTo(translateUrl)
                },
                onToggleDesktopMode = {
                    isDesktopSite = !isDesktopSite
                    webViewInstance?.settings?.let { s ->
                        if (isDesktopSite) {
                            s.userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                            s.useWideViewPort = true
                            s.loadWithOverviewMode = true
                        } else {
                            s.userAgentString = null
                        }
                        webViewInstance?.reload()
                    }
                    Toast.makeText(context, if (isDesktopSite) "Desktop site requested" else "Mobile site requested", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

package com.niooon.browser.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niooon.browser.R
import com.niooon.browser.model.DiscoverArticle
import com.niooon.browser.model.ShortcutItem
import com.niooon.browser.ui.components.DefaultArticles
import com.niooon.browser.ui.components.DefaultShortcuts
import com.niooon.browser.ui.components.DiscoverFeed
import com.niooon.browser.ui.components.FloatingGlassDock
import com.niooon.browser.ui.components.GlassSearchBar
import com.niooon.browser.ui.components.GoogleBrandHeader
import com.niooon.browser.ui.components.LiquidGlassBackground
import com.niooon.browser.ui.components.ShortcutGrid
import com.niooon.browser.ui.components.TopStatusBar
import com.niooon.browser.ui.theme.GoogleBlue
import com.niooon.browser.ui.theme.TextPrimary
import com.niooon.browser.ui.theme.TextSecondary

@Composable
fun BrowserHomeScreen(
    onNavigateToUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val shortcuts = remember { mutableStateListOf<ShortcutItem>().apply { addAll(DefaultShortcuts) } }

    var showAddDialog by remember { mutableStateOf(false) }
    var newShortcutTitle by remember { mutableStateOf("") }
    var newShortcutUrl by remember { mutableStateOf("") }

    var showMenuDialog by remember { mutableStateOf(false) }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var showLensDialog by remember { mutableStateOf(false) }

    fun executeSearch(rawQuery: String) {
        val trimmed = rawQuery.trim()
        if (trimmed.isEmpty()) return

        val targetUrl = if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            trimmed
        } else if (trimmed.contains(".") && !trimmed.contains(" ")) {
            "https://$trimmed"
        } else {
            "https://www.google.com/search?q=" + java.net.URLEncoder.encode(trimmed, "UTF-8")
        }
        onNavigateToUrl(targetUrl)
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 1. 3D Liquid Glass Canvas Background
        LiquidGlassBackground()

        // 2. Scrollable Body Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Bar (9:41 + Battery + Profile)
            TopStatusBar(
                onProfileClick = {
                    Toast.makeText(context, "Logged into Google Account", Toast.LENGTH_SHORT).show()
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Google Colorful Brand Logo
            GoogleBrandHeader()

            Spacer(modifier = Modifier.height(10.dp))

            // Glass Smart Omnibox Search Bar
            GlassSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { executeSearch(it) },
                onVoiceClick = { showVoiceDialog = true },
                onLensClick = { showLensDialog = true }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4x2 Shortcuts Grid
            ShortcutGrid(
                shortcuts = shortcuts,
                onShortcutClick = { item ->
                    if (item.url.isNotEmpty()) {
                        onNavigateToUrl(item.url)
                    }
                },
                onAddShortcut = { showAddDialog = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Discover News Feed
            DiscoverFeed(
                articles = DefaultArticles,
                onArticleClick = { article: DiscoverArticle ->
                    onNavigateToUrl(article.articleUrl)
                },
                onSeeMoreClick = {
                    onNavigateToUrl("https://news.google.com")
                }
            )

            // Padding space so bottom dock does not overlap content
            Spacer(modifier = Modifier.height(110.dp))
        }

        // 3. Floating Frosted Glass Bottom Dock (Fixed at bottom)
        FloatingGlassDock(
            currentTabCount = 1,
            canGoBack = false,
            canGoForward = false,
            onBackClick = { },
            onForwardClick = { },
            onSearchClick = {
                // Focus search bar
                Toast.makeText(context, "Search activated", Toast.LENGTH_SHORT).show()
            },
            onTabsClick = {
                Toast.makeText(context, "Tabs manager: 1 Active Tab", Toast.LENGTH_SHORT).show()
            },
            onMenuClick = { showMenuDialog = true },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Dialog: Add Shortcut
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Shortcut", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newShortcutTitle,
                            onValueChange = { newShortcutTitle = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newShortcutUrl,
                            onValueChange = { newShortcutUrl = it },
                            label = { Text("URL (e.g. github.com)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newShortcutTitle.isNotBlank() && newShortcutUrl.isNotBlank()) {
                                val cleanUrl = if (newShortcutUrl.startsWith("http")) newShortcutUrl else "https://$newShortcutUrl"
                                val newItem = ShortcutItem(
                                    id = System.currentTimeMillis().toString(),
                                    title = newShortcutTitle,
                                    url = cleanUrl,
                                    iconRes = R.drawable.ic_search
                                )
                                // Insert before the "Add" button
                                val addIndex = shortcuts.indexOfFirst { it.isSystemAdd }
                                if (addIndex != -1) {
                                    shortcuts.add(addIndex, newItem)
                                } else {
                                    shortcuts.add(newItem)
                                }
                                newShortcutTitle = ""
                                newShortcutUrl = ""
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoogleBlue)
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }

        // Dialog: Voice Search
        if (showVoiceDialog) {
            AlertDialog(
                onDismissRequest = { showVoiceDialog = false },
                title = { Text("Google Voice Search", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = "Mic",
                            tint = Color(0xFFEA4335),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Listening for search query...", color = TextSecondary)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showVoiceDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = GoogleBlue)
                    ) {
                        Text("Done")
                    }
                }
            )
        }

        // Dialog: Google Lens
        if (showLensDialog) {
            AlertDialog(
                onDismissRequest = { showLensDialog = false },
                title = { Text("Google Lens", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "Camera",
                            tint = GoogleBlue,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Visual search with your camera or gallery", color = TextSecondary)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showLensDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = GoogleBlue)
                    ) {
                        Text("Open Camera")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLensDialog = false }) {
                        Text("Close", color = TextSecondary)
                    }
                }
            )
        }

        // Dialog: Browser Menu
        if (showMenuDialog) {
            AlertDialog(
                onDismissRequest = { showMenuDialog = false },
                title = { Text("niooonu browser", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column {
                        listOf(
                            "New tab",
                            "New incognito tab",
                            "Bookmarks",
                            "Recent tabs",
                            "History",
                            "Downloads",
                            "Desktop site",
                            "Settings"
                        ).forEach { item ->
                            TextButton(
                                onClick = {
                                    Toast.makeText(context, "$item selected", Toast.LENGTH_SHORT).show()
                                    showMenuDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(item, color = TextPrimary, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showMenuDialog = false }) {
                        Text("Close", color = TextSecondary)
                    }
                }
            )
        }
    }
}

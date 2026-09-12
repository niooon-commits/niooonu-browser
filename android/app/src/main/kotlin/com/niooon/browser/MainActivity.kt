package com.niooon.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.niooon.browser.model.BrowserTab
import com.niooon.browser.ui.components.TabSwitcherDialog
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
                    // Manage multiple tabs
                    val tabs = remember {
                        mutableStateListOf(
                            BrowserTab(title = "Home", url = "")
                        )
                    }
                    var activeTabIndex by remember { mutableIntStateOf(0) }
                    var isShowingHomeScreen by remember { mutableStateOf(true) }
                    var showTabSwitcher by remember { mutableStateOf(false) }

                    val currentTab = tabs.getOrNull(activeTabIndex)
                        ?: tabs.firstOrNull()
                        ?: BrowserTab().also { tabs.add(it) }

                    // When searching/navigating, transition away from Home screen and remove bottom navigation
                    if (isShowingHomeScreen || currentTab.url.isEmpty()) {
                        BrowserHomeScreen(
                            tabCount = tabs.size,
                            onNavigateToUrl = { url ->
                                val updated = currentTab.copy(url = url, title = url)
                                if (activeTabIndex in tabs.indices) {
                                    tabs[activeTabIndex] = updated
                                } else {
                                    tabs.add(updated)
                                    activeTabIndex = tabs.lastIndex
                                }
                                isShowingHomeScreen = false
                            },
                            onTabsClick = {
                                showTabSwitcher = true
                            }
                        )
                    } else {
                        // In-Browser Screen matching requested screenshot:
                        // Top bar has: Home button, Pill URL, New tab (+), Tab counter badge [N], 3-dots menu
                        // Bottom navigation is completely removed!
                        WebViewScreen(
                            initialUrl = currentTab.url,
                            tabCount = tabs.size,
                            onHomeClick = {
                                isShowingHomeScreen = true
                            },
                            onNewTabClick = {
                                val newTab = BrowserTab(title = "New Tab", url = "")
                                tabs.add(newTab)
                                activeTabIndex = tabs.lastIndex
                                isShowingHomeScreen = true
                            },
                            onTabsClick = {
                                showTabSwitcher = true
                            },
                            onCloseWebView = {
                                if (tabs.size > 1) {
                                    tabs.removeAt(activeTabIndex)
                                    activeTabIndex = (activeTabIndex - 1).coerceAtLeast(0)
                                } else {
                                    tabs[0] = BrowserTab(title = "Home", url = "")
                                    isShowingHomeScreen = true
                                }
                            }
                        )
                    }

                    // Liquid Glass Multi-Tab Switcher Dialog
                    if (showTabSwitcher) {
                        TabSwitcherDialog(
                            tabs = tabs,
                            activeTabId = currentTab.id,
                            onSelectTab = { selectedId ->
                                val idx = tabs.indexOfFirst { it.id == selectedId }
                                if (idx != -1) {
                                    activeTabIndex = idx
                                    val tab = tabs[idx]
                                    isShowingHomeScreen = tab.url.isEmpty()
                                }
                                showTabSwitcher = false
                            },
                            onCloseTab = { closeId ->
                                val idx = tabs.indexOfFirst { it.id == closeId }
                                if (idx != -1) {
                                    if (tabs.size > 1) {
                                        tabs.removeAt(idx)
                                        if (activeTabIndex >= tabs.size) {
                                            activeTabIndex = tabs.size - 1
                                        }
                                    } else {
                                        tabs[0] = BrowserTab(title = "Home", url = "")
                                        isShowingHomeScreen = true
                                    }
                                }
                            },
                            onNewTab = {
                                val newTab = BrowserTab(title = "New Tab", url = "")
                                tabs.add(newTab)
                                activeTabIndex = tabs.lastIndex
                                isShowingHomeScreen = true
                                showTabSwitcher = false
                            },
                            onDismiss = {
                                showTabSwitcher = false
                            }
                        )
                    }
                }
            }
        }
    }
}

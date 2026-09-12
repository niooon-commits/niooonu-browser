package com.niooon.browser.model

import androidx.annotation.DrawableRes

data class ShortcutItem(
    val id: String,
    val title: String,
    val url: String,
    @DrawableRes val iconRes: Int,
    val isSystemAdd: Boolean = false
)

data class DiscoverArticle(
    val id: String,
    val title: String,
    val category: String,
    val timeAgo: String,
    val imageUrl: String,
    val articleUrl: String
)

data class BrowserTab(
    val id: String,
    val title: String,
    val url: String,
    val isCurrent: Boolean = false
)

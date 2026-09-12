package com.niooon.browser.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AddBox
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.DesktopWindows
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niooon.browser.R

@Composable
fun BrowserPopupMenu(
    isDesktopMode: Boolean,
    onDismiss: () -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onBookmark: () -> Unit,
    onDownload: () -> Unit,
    onReload: () -> Unit,
    onNewTab: () -> Unit,
    onNewIncognitoTab: () -> Unit,
    onMoveTabToGroup: () -> Unit,
    onManageWindows: () -> Unit,
    onHistory: () -> Unit,
    onDeleteBrowsingData: () -> Unit,
    onSiteControls: () -> Unit,
    onBlockDomain: () -> Unit = {},
    onDomainBlockList: () -> Unit = {},
    onDownloadsList: () -> Unit,
    onBookmarksList: () -> Unit,
    onRecentTabs: () -> Unit,
    onShare: () -> Unit,
    onFindInPage: () -> Unit,
    onTranslate: () -> Unit,
    onToggleDesktopMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isBookmarked by remember { mutableStateOf(false) }

    // Full screen overlay backdrop for outside touch dismiss
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x33000000))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            )
            .statusBarsPadding()
    ) {
        // Elevated Dark Material Card aligned to Top-End right under top bar
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(140)) + scaleIn(
                animationSpec = tween(160),
                transformOrigin = TransformOrigin(0.9f, 0f)
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 58.dp, end = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(285.dp)
                    .heightIn(max = 620.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color(0xFF232529)) // Dark Material 3 Surface
                    .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(26.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {} // Intercept clicks inside card
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 1. TOP ACTION BUTTONS ROW (Back, Forward, Star, Download, Reload)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF2A2D32))
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back
                        CircleActionButton(
                            icon = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            onClick = {
                                onBack()
                                onDismiss()
                            }
                        )

                        // Forward
                        CircleActionButton(
                            icon = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "Forward",
                            onClick = {
                                onForward()
                                onDismiss()
                            }
                        )

                        // Bookmark
                        CircleActionButton(
                            icon = if (isBookmarked) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) Color(0xFFFFB74D) else Color(0xFFE2E8F0),
                            onClick = {
                                isBookmarked = !isBookmarked
                                onBookmark()
                            }
                        )

                        // Download
                        CircleActionButton(
                            icon = Icons.Rounded.FileDownload,
                            contentDescription = "Download page",
                            onClick = {
                                onDownload()
                                onDismiss()
                            }
                        )

                        // Reload
                        CircleActionButton(
                            icon = Icons.Rounded.Refresh,
                            contentDescription = "Reload",
                            onClick = {
                                onReload()
                                onDismiss()
                            }
                        )
                    }

                    HorizontalDivider(color = Color(0x1AFFFFFF), thickness = 0.8.dp)

                    // 2. SCROLLABLE MENU ITEMS LIST
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                    ) {
                        // New tab
                        BrowserMenuItem(
                            icon = Icons.Rounded.AddBox,
                            title = "New tab",
                            onClick = {
                                onNewTab()
                                onDismiss()
                            }
                        )

                        // New Incognito tab
                        BrowserMenuItem(
                            icon = Icons.Rounded.VisibilityOff,
                            title = "New Incognito tab",
                            onClick = {
                                onNewIncognitoTab()
                                onDismiss()
                            }
                        )

                        // Move tab to group
                        BrowserMenuItem(
                            icon = Icons.Rounded.GridView,
                            title = "Move tab to group",
                            onClick = {
                                onMoveTabToGroup()
                                onDismiss()
                            }
                        )

                        // Manage windows
                        BrowserMenuItem(
                            icon = Icons.Rounded.Layers,
                            title = "Manage windows",
                            onClick = {
                                onManageWindows()
                                onDismiss()
                            }
                        )

                        MenuDivider()

                        // History
                        BrowserMenuItem(
                            icon = Icons.Rounded.History,
                            title = "History",
                            onClick = {
                                onHistory()
                                onDismiss()
                            }
                        )

                        // Delete browsing data
                        BrowserMenuItem(
                            icon = Icons.Rounded.DeleteOutline,
                            title = "Delete browsing data",
                            onClick = {
                                onDeleteBrowsingData()
                                onDismiss()
                            }
                        )

                        // Site controls
                        BrowserMenuItem(
                            icon = Icons.Rounded.Tune,
                            title = "Site controls",
                            onClick = {
                                onSiteControls()
                                onDismiss()
                            }
                        )

                        // Block domain (ডোমেইন ব্লক)
                        BrowserMenuItem(
                            icon = Icons.Rounded.Block,
                            title = "Block domain",
                            tint = Color(0xFFF87171),
                            onClick = {
                                onBlockDomain()
                                onDismiss()
                            }
                        )

                        // Domain block list (ডোমেইন ব্লক লিস্ট)
                        BrowserMenuItem(
                            icon = Icons.Rounded.Shield,
                            title = "Domain block list",
                            tint = Color(0xFF60A5FA),
                            onClick = {
                                onDomainBlockList()
                                onDismiss()
                            }
                        )

                        MenuDivider()

                        // Downloads
                        BrowserMenuItem(
                            icon = Icons.Rounded.FileDownload,
                            title = "Downloads",
                            onClick = {
                                onDownloadsList()
                                onDismiss()
                            }
                        )

                        // Bookmarks
                        BrowserMenuItem(
                            icon = Icons.Rounded.BookmarkBorder,
                            title = "Bookmarks",
                            onClick = {
                                onBookmarksList()
                                onDismiss()
                            }
                        )

                        // Recent tabs
                        BrowserMenuItem(
                            icon = Icons.Rounded.Devices,
                            title = "Recent tabs",
                            onClick = {
                                onRecentTabs()
                                onDismiss()
                            }
                        )

                        MenuDivider()

                        // Share... (with WhatsApp badge on the right!)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = Color.White),
                                    onClick = {
                                        onShare()
                                        onDismiss()
                                    }
                                )
                                .padding(horizontal = 14.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Share,
                                    contentDescription = "Share",
                                    tint = Color(0xFFCBD5E1),
                                    modifier = Modifier.size(21.dp)
                                )
                                Text(
                                    text = "Share...",
                                    color = Color(0xFFF1F5F9),
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            // Circular WhatsApp badge as shown in user screenshot
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF25D366)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_whatsapp),
                                    contentDescription = "WhatsApp",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Find in page
                        BrowserMenuItem(
                            icon = Icons.Rounded.Search,
                            title = "Find in page",
                            onClick = {
                                onFindInPage()
                                onDismiss()
                            }
                        )

                        // Translate...
                        BrowserMenuItem(
                            icon = Icons.Rounded.Translate,
                            title = "Translate...",
                            onClick = {
                                onTranslate()
                                onDismiss()
                            }
                        )

                        // Desktop site toggle with clean checkbox
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = Color.White),
                                    onClick = {
                                        onToggleDesktopMode()
                                        onDismiss()
                                    }
                                )
                                .padding(horizontal = 14.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DesktopWindows,
                                    contentDescription = "Desktop site",
                                    tint = Color(0xFFCBD5E1),
                                    modifier = Modifier.size(21.dp)
                                )
                                Text(
                                    text = "Desktop site",
                                    color = Color(0xFFF1F5F9),
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(19.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isDesktopMode) Color(0xFF3B82F6) else Color(0x1AFFFFFF))
                                    .border(
                                        1.dp,
                                        if (isDesktopMode) Color(0xFF3B82F6) else Color(0x44FFFFFF),
                                        RoundedCornerShape(4.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDesktopMode) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = "Checked",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CircleActionButton(
    icon: ImageVector,
    contentDescription: String,
    tint: Color = Color(0xFFE2E8F0),
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0x0FFFFFFF))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = Color.White),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(21.dp)
        )
    }
}

@Composable
private fun BrowserMenuItem(
    icon: ImageVector,
    title: String,
    tint: Color = Color(0xFFCBD5E1),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = Color.White),
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = tint,
            modifier = Modifier.size(21.dp)
        )
        Text(
            text = title,
            color = Color(0xFFF1F5F9),
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        color = Color(0x18FFFFFF),
        thickness = 0.8.dp,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp)
    )
}

package com.niooon.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niooon.browser.R
import com.niooon.browser.model.ShortcutItem
import com.niooon.browser.ui.theme.TextPrimary

val DefaultShortcuts = listOf(
    ShortcutItem("yt", "YouTube", "https://www.youtube.com", R.drawable.ic_youtube),
    ShortcutItem("ig", "Instagram", "https://www.instagram.com", R.drawable.ic_instagram),
    ShortcutItem("fb", "Facebook", "https://www.facebook.com", R.drawable.ic_facebook),
    ShortcutItem("wa", "WhatsApp", "https://web.whatsapp.com", R.drawable.ic_whatsapp),
    ShortcutItem("gg", "Google", "https://www.google.com", R.drawable.ic_google),
    ShortcutItem("x", "X (Twitter)", "https://x.com", R.drawable.ic_x_twitter),
    ShortcutItem("pin", "Pinterest", "https://www.pinterest.com", R.drawable.ic_pinterest),
    ShortcutItem("add", "Add", "", R.drawable.ic_add, isSystemAdd = true)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShortcutGrid(
    shortcuts: List<ShortcutItem> = DefaultShortcuts,
    onShortcutClick: (ShortcutItem) -> Unit,
    onAddShortcut: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        maxItemsInEachRow = 4
    ) {
        shortcuts.forEach { item ->
            ShortcutTile(
                item = item,
                onClick = {
                    if (item.isSystemAdd) onAddShortcut() else onShortcutClick(item)
                }
            )
        }
    }
}

@Composable
fun ShortcutTile(
    item: ShortcutItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val squircleShape = RoundedCornerShape(22.dp)

    Column(
        modifier = modifier
            .width(76.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 3D Frosted Glass Squircle Base
        Box(
            modifier = Modifier
                .size(62.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = squircleShape,
                    ambientColor = Color(0x264285F4),
                    spotColor = Color(0x1F000000)
                )
                .clip(squircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xE6FFFFFF),
                            Color(0x99FFFFFF)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0x66FFFFFF)
                        )
                    ),
                    shape = squircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (item.isSystemAdd) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = item.title,
                    tint = Color(0xFF475569),
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.title,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Label below
        Text(
            text = item.title,
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

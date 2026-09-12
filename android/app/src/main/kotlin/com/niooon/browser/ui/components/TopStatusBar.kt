package com.niooon.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niooon.browser.R
import com.niooon.browser.ui.theme.TextPrimary

@Composable
fun TopStatusBar(
    onProfileClick: () -> Unit = {},
    onDownloadsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Clean Brand Accent
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FFFFFF), CircleShape)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "niooonu",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        // Right side: Quick Actions (Downloads + Profile)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Quick Downloads shortcut icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x99FFFFFF))
                    .border(1.dp, Color(0xB3FFFFFF), CircleShape)
                    .clickable { onDownloadsClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = "Downloads",
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Frosted Glass User Profile Circle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x99FFFFFF))
                    .border(1.dp, Color(0xB3FFFFFF), CircleShape)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_account_circle),
                    contentDescription = "User Profile",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

package com.niooon.browser.ui.screens

import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DownloadedItem(
    val id: String,
    val name: String,
    val sizeText: String,
    val dateText: String,
    val path: String,
    val isApk: Boolean = false
)

val HexagonBadgeShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    moveTo(w * 0.5f, 0f)
    lineTo(w, h * 0.25f)
    lineTo(w, h * 0.75f)
    lineTo(w * 0.5f, h)
    lineTo(0f, h * 0.75f)
    lineTo(0f, h * 0.25f)
    close()
}

@Composable
fun DownloadsScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }
    var selectedItemForAction by remember { mutableStateOf<DownloadedItem?>(null) }
    val downloadsList = remember { mutableStateListOf<DownloadedItem>() }
    var storageInfoText by remember { mutableStateOf("Using 0.00 KB of 110.77 GB") }

    // Load available storage info and any downloaded files in download folder
    LaunchedEffect(Unit) {
        storageInfoText = getStorageUsageInfo(context)
        loadDownloads(context, downloadsList)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF140C0B)) // Dark background matching exact screenshot
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. TOP HEADER (Title: Downloads, Settings gear, Close cross)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 12.dp, top = 14.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Downloads",
                    color = Color(0xFFF1F5F9),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.2.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Settings Gear Button
                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFFE2E8F0),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Close Cross Button
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFE2E8F0),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // 2. SUBTITLE ("Using 0.00 KB of 110.77 GB")
            Text(
                text = storageInfoText,
                color = Color(0xFFB0A4A4),
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 3. MAIN BODY (Empty state or Downloaded items list)
            if (downloadsList.isEmpty()) {
                // EXACT REPRODUCTION OF USER SCREENSHOT
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Blue Hexagon Badge with Download Arrow Icon
                        Box(
                            modifier = Modifier
                                .size(112.dp)
                                .clip(HexagonBadgeShape)
                                .background(Color(0xFF2563EB)), // Royal Blue matching screenshot
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.FileDownload,
                                contentDescription = "Download Arrow",
                                tint = Color.White,
                                modifier = Modifier.size(56.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Main Header Text
                        Text(
                            text = "You'll find your downloads here",
                            color = Color(0xFFF8FAFC),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Description Text
                        Text(
                            text = "You can save images and files to view offline or share in other apps",
                            color = Color(0xFF94A3B8),
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            } else {
                // Downloaded files list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(downloadsList, key = { it.id }) { item ->
                        DownloadItemRow(
                            item = item,
                            onClick = {
                                Toast.makeText(context, "Opening ${item.name}", Toast.LENGTH_SHORT).show()
                            },
                            onMoreClick = {
                                selectedItemForAction = item
                            }
                        )
                        HorizontalDivider(
                            color = Color(0x18FFFFFF),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Settings Dialog
        if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                containerColor = Color(0xFF232529),
                title = {
                    Text("Download Settings", fontWeight = FontWeight.SemiBold, color = Color.White)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Download location:",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/niooonu",
                            color = Color(0xFF38BDF8),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = storageInfoText,
                            color = Color(0xFFCBD5E1),
                            fontSize = 13.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showSettingsDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }

        // Item Actions Menu Dialog
        selectedItemForAction?.let { item ->
            AlertDialog(
                onDismissRequest = { selectedItemForAction = null },
                containerColor = Color(0xFF232529),
                title = {
                    Text(
                        text = item.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                text = {
                    Column {
                        TextButton(
                            onClick = {
                                Toast.makeText(context, "Sharing ${item.name}", Toast.LENGTH_SHORT).show()
                                selectedItemForAction = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.Share, contentDescription = "Share", tint = Color(0xFF60A5FA))
                                Text("Share", color = Color.White)
                            }
                        }

                        TextButton(
                            onClick = {
                                try {
                                    val file = File(item.path)
                                    if (file.exists()) file.delete()
                                    downloadsList.remove(item)
                                    Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
                                } catch (_: Exception) {
                                    Toast.makeText(context, "Could not delete file", Toast.LENGTH_SHORT).show()
                                }
                                selectedItemForAction = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color(0xFFF87171))
                                Text("Delete", color = Color(0xFFF87171))
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedItemForAction = null }) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                }
            )
        }
    }
}

@Composable
private fun DownloadItemRow(
    item: DownloadedItem,
    onClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (item.isApk) Color(0xFF065F46) else Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.isApk) Icons.Rounded.Android else Icons.Rounded.InsertDriveFile,
                    contentDescription = "File",
                    tint = if (item.isApk) Color(0xFF34D399) else Color(0xFF94A3B8),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    color = Color(0xFFF1F5F9),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.sizeText} • ${item.dateText}",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.5.sp
                )
            }
        }

        IconButton(onClick = onMoreClick, modifier = Modifier.size(36.dp)) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "More",
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun getStorageUsageInfo(context: Context): String {
    return try {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val totalBytes = totalBlocks * blockSize
        val totalGB = totalBytes.toDouble() / (1024 * 1024 * 1024)

        // Calculate size of niooonu downloads directory
        val downloadDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "niooonu"
        )
        val usedBytes = if (downloadDir.exists()) getFolderSize(downloadDir) else 0L

        val usedKB = usedBytes.toDouble() / 1024.0
        val usedMB = usedKB / 1024.0

        val formattedUsed = if (usedMB >= 1.0) {
            String.format(Locale.US, "%.2f MB", usedMB)
        } else {
            String.format(Locale.US, "%.2f KB", usedKB)
        }

        String.format(Locale.US, "Using %s of %.2f GB", formattedUsed, totalGB)
    } catch (_: Exception) {
        "Using 0.00 KB of 110.77 GB"
    }
}

private fun getFolderSize(dir: File): Long {
    var size = 0L
    dir.listFiles()?.forEach { file ->
        size += if (file.isDirectory) getFolderSize(file) else file.length()
    }
    return size
}

private fun loadDownloads(context: Context, list: MutableList<DownloadedItem>) {
    try {
        val downloadDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "niooonu"
        )
        if (downloadDir.exists() && downloadDir.isDirectory) {
            val files = downloadDir.listFiles() ?: return
            val df = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val items = files.filter { it.isFile }.map { f ->
                val sizeKb = f.length() / 1024.0
                val sizeMb = sizeKb / 1024.0
                val sizeText = if (sizeMb >= 1.0) {
                    String.format(Locale.US, "%.2f MB", sizeMb)
                } else {
                    String.format(Locale.US, "%.1f KB", sizeKb)
                }
                DownloadedItem(
                    id = f.name,
                    name = f.name,
                    sizeText = sizeText,
                    dateText = df.format(Date(f.lastModified())),
                    path = f.absolutePath,
                    isApk = f.name.endsWith(".apk", ignoreCase = true)
                )
            }
            list.clear()
            list.addAll(items.sortedByDescending { it.dateText })
        }
    } catch (_: Exception) {
        // empty downloads
    }
}

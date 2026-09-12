package com.niooon.browser.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.StatFs
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niooon.browser.download.DownloadStatus
import com.niooon.browser.download.DownloadTask
import com.niooon.browser.download.NiooonuDownloadManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Symmetrical Hexagon Badge Shape
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

data class DownloadedItem(
    val id: String,
    val name: String,
    val sizeText: String,
    val dateText: String,
    val path: String,
    val isApk: Boolean = false,
    val category: String = "other"
)

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
    var selectedCategory by remember { mutableStateOf("All") }

    val activeDownloads by NiooonuDownloadManager.activeDownloads.collectAsState()
    val runningTasks = activeDownloads.filter {
        it.status == DownloadStatus.RUNNING || it.status == DownloadStatus.PENDING
    }

    // Load storage and downloads
    LaunchedEffect(activeDownloads) {
        storageInfoText = getStorageUsageInfo(context)
        loadDownloads(context, downloadsList)
    }

    val categories = listOf("All", "APK", "Images", "Videos", "Docs")

    val filteredList = downloadsList.filter { item ->
        when (selectedCategory) {
            "All" -> true
            "APK" -> item.isApk
            "Images" -> item.category == "image"
            "Videos" -> item.category == "video"
            "Docs" -> item.category == "doc"
            else -> true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF140C0B)) // Dark sleek background
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

            // 2. SUBTITLE (Storage Info)
            Text(
                text = storageInfoText,
                color = Color(0xFFB0A4A4),
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. CATEGORY PILLS ROW
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Color(0xFF2563EB) else Color(0xFF232529))
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFF3B82F6) else Color(0x33FFFFFF),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. ACTIVE RUNNING DOWNLOADS SECTION (Real-time progress bars)
            AnimatedVisibility(visible = runningTasks.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Downloading (${runningTasks.size})",
                        color = Color(0xFF60A5FA),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    runningTasks.forEach { task ->
                        ActiveDownloadCard(
                            task = task,
                            onCancel = {
                                NiooonuDownloadManager.cancelDownload(context, task.id)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // 5. MAIN CONTENT (Empty state OR Completed list)
            if (filteredList.isEmpty() && runningTasks.isEmpty()) {
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
                        Box(
                            modifier = Modifier
                                .size(112.dp)
                                .clip(HexagonBadgeShape)
                                .background(Color(0xFF2563EB)),
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

                        Text(
                            text = "You'll find your downloads here",
                            color = Color(0xFFF8FAFC),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "You can save images, APKs and files to view offline or share with other apps",
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        DownloadItemRow(
                            item = item,
                            onClick = {
                                val file = File(item.path)
                                if (file.exists()) {
                                    NiooonuDownloadManager.openFile(context, file)
                                } else {
                                    Toast.makeText(context, "File does not exist", Toast.LENGTH_SHORT).show()
                                }
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
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Download location:",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "/Download/Niooonu",
                            color = Color(0xFF60A5FA),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Real-time notifications enabled\n• Background downloads enabled\n• Automatic mime detection",
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showSettingsDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Text("Done")
                    }
                }
            )
        }

        // Item Actions Dialog (Open, Share, Delete)
        selectedItemForAction?.let { item ->
            AlertDialog(
                onDismissRequest = { selectedItemForAction = null },
                containerColor = Color(0xFF232529),
                title = {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedItemForAction = null
                                    val file = File(item.path)
                                    if (file.exists()) {
                                        NiooonuDownloadManager.openFile(context, file)
                                    }
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Rounded.OpenInNew, "Open", tint = Color(0xFF60A5FA))
                            Text("Open file", color = Color.White, fontSize = 15.sp)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedItemForAction = null
                                    shareDownloadedFile(context, item)
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Rounded.Share, "Share", tint = Color(0xFF34D399))
                            Text("Share file", color = Color.White, fontSize = 15.sp)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedItemForAction = null
                                    val file = File(item.path)
                                    if (file.exists()) {
                                        file.delete()
                                        downloadsList.remove(item)
                                        Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Rounded.Delete, "Delete", tint = Color(0xFFF87171))
                            Text("Delete from storage", color = Color(0xFFF87171), fontSize = 15.sp)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedItemForAction = null }) {
                        Text("Close", color = Color(0xFF94A3B8))
                    }
                }
            )
        }
    }
}

@Composable
private fun ActiveDownloadCard(
    task: DownloadTask,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E2430))
            .border(1.dp, Color(0x443B82F6), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FileDownload,
                            contentDescription = "Downloading",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.fileName,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${task.speedText} • ${task.progress}%",
                            color = Color(0xFF93C5FD),
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(onClick = onCancel, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Cancel",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Real-time Progress Bar
            LinearProgressIndicator(
                progress = { task.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF3B82F6),
                trackColor = Color(0xFF334155)
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
            val (icon, bg, tint) = getCategoryIconAndColor(item)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "File",
                    tint = tint,
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

private fun getCategoryIconAndColor(item: DownloadedItem): Triple<ImageVector, Color, Color> {
    return when {
        item.isApk -> Triple(Icons.Rounded.Android, Color(0xFF065F46), Color(0xFF34D399))
        item.category == "image" -> Triple(Icons.Rounded.Image, Color(0xFF4C1D95), Color(0xFFA78BFA))
        item.category == "video" -> Triple(Icons.Rounded.Movie, Color(0xFF831843), Color(0xFFF472B6))
        else -> Triple(Icons.Rounded.InsertDriveFile, Color(0xFF1E293B), Color(0xFF94A3B8))
    }
}

private fun shareDownloadedFile(context: Context, item: DownloadedItem) {
    try {
        val file = File(item.path)
        if (!file.exists()) return
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = NiooonuDownloadManager.resolveMimeType(item.name)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share ${item.name}"))
    } catch (e: Exception) {
        Toast.makeText(context, "Could not share: ${e.message}", Toast.LENGTH_SHORT).show()
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

        val downloadDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Niooonu"
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
        val dirs = listOf(
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Niooonu"),
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "niooonu")
        )

        val files = mutableListOf<File>()
        dirs.forEach { d ->
            if (d.exists() && d.isDirectory) {
                d.listFiles()?.let { files.addAll(it) }
            }
        }

        if (files.isNotEmpty()) {
            val df = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val items = files.filter { it.isFile }.map { f ->
                val sizeKb = f.length() / 1024.0
                val sizeMb = sizeKb / 1024.0
                val sizeText = if (sizeMb >= 1.0) {
                    String.format(Locale.US, "%.2f MB", sizeMb)
                } else {
                    String.format(Locale.US, "%.1f KB", sizeKb)
                }
                val ext = f.name.substringAfterLast('.', "").lowercase()
                val category = when (ext) {
                    "apk" -> "apk"
                    "jpg", "jpeg", "png", "webp", "gif" -> "image"
                    "mp4", "mkv", "mov", "webm" -> "video"
                    "pdf", "doc", "docx", "txt", "zip", "rar" -> "doc"
                    else -> "other"
                }

                DownloadedItem(
                    id = f.name,
                    name = f.name,
                    sizeText = sizeText,
                    dateText = df.format(Date(f.lastModified())),
                    path = f.absolutePath,
                    isApk = ext == "apk",
                    category = category
                )
            }
            list.clear()
            list.addAll(items.sortedByDescending { it.dateText })
        }
    } catch (_: Exception) {
        // empty downloads
    }
}

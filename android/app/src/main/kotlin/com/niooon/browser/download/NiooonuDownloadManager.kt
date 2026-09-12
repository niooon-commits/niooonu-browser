package com.niooon.browser.download

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

enum class DownloadStatus {
    PENDING,
    RUNNING,
    PAUSED,
    SUCCESSFUL,
    FAILED
}

data class DownloadTask(
    val id: Long,
    val fileName: String,
    val url: String,
    val totalBytes: Long,
    val downloadedBytes: Long,
    val progress: Int,
    val status: DownloadStatus,
    val speedText: String,
    val mimeType: String,
    val localUri: String? = null,
    val filePath: String? = null,
    val dateAdded: Long = System.currentTimeMillis()
)

object NiooonuDownloadManager {
    private val _activeDownloads = MutableStateFlow<List<DownloadTask>>(emptyList())
    val activeDownloads: StateFlow<List<DownloadTask>> = _activeDownloads.asStateFlow()

    private var trackerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    fun startDownload(
        context: Context,
        url: String,
        userAgent: String? = null,
        contentDisposition: String? = null,
        mimetype: String? = null,
        contentLength: Long = 0L
    ): Long {
        try {
            if (url.isBlank()) {
                Toast.makeText(context, "Invalid download URL", Toast.LENGTH_SHORT).show()
                return -1L
            }

            // Clean & guess file name
            var resolvedFileName = URLUtil.guessFileName(url, contentDisposition, mimetype)
            if (resolvedFileName.isNullOrBlank() || resolvedFileName == "downloadfile.bin") {
                val lastPath = Uri.parse(url).lastPathSegment
                if (!lastPath.isNullOrBlank() && lastPath.contains(".")) {
                    resolvedFileName = lastPath
                } else {
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimetype) ?: "bin"
                    resolvedFileName = "download_${System.currentTimeMillis()}.$extension"
                }
            }

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
                ?: run {
                    Toast.makeText(context, "System DownloadManager unavailable", Toast.LENGTH_SHORT).show()
                    return -1L
                }

            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri).apply {
                setTitle(resolvedFileName)
                setDescription("Downloading in Niooonu Browser...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)

                if (!userAgent.isNullOrBlank()) {
                    addRequestHeader("User-Agent", userAgent)
                }

                try {
                    // Place inside Public Downloads/Niooonu
                    setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "Niooonu/$resolvedFileName"
                    )
                } catch (e: Exception) {
                    try {
                        setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            resolvedFileName
                        )
                    } catch (e2: Exception) {
                        // Fallback to app-specific external files dir
                        setDestinationInExternalFilesDir(
                            context,
                            Environment.DIRECTORY_DOWNLOADS,
                            resolvedFileName
                        )
                    }
                }
            }

            val downloadId = downloadManager.enqueue(request)

            val initialTask = DownloadTask(
                id = downloadId,
                fileName = resolvedFileName,
                url = url,
                totalBytes = contentLength,
                downloadedBytes = 0L,
                progress = 0,
                status = DownloadStatus.PENDING,
                speedText = "Starting...",
                mimeType = mimetype ?: resolveMimeType(resolvedFileName)
            )

            val currentList = _activeDownloads.value.toMutableList()
            currentList.add(0, initialTask)
            _activeDownloads.value = currentList

            startTrackingProgress(context)
            Toast.makeText(context, "Downloading $resolvedFileName", Toast.LENGTH_SHORT).show()
            return downloadId
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Download failed: ${e.localizedMessage ?: "Unknown error"}", Toast.LENGTH_LONG).show()
            return -1L
        }
    }

    fun cancelDownload(context: Context, downloadId: Long) {
        try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
            downloadManager?.remove(downloadId)

            val updated = _activeDownloads.value.filterNot { it.id == downloadId }
            _activeDownloads.value = updated
            Toast.makeText(context, "Download cancelled", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openDownloadedFile(context: Context, task: DownloadTask) {
        val path = task.filePath
        if (!path.isNullOrBlank()) {
            val file = File(path)
            if (file.exists()) {
                openFile(context, file, task.mimeType)
                return
            }
        }

        // Try using localUri
        if (!task.localUri.isNullOrBlank()) {
            try {
                val uri = Uri.parse(task.localUri)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, task.mimeType)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                return
            } catch (e: Exception) {
                // Ignore and fallback to file search
            }
        }

        // Search in Niooonu download directory
        val niooonuDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Niooonu")
        val candidate = File(niooonuDir, task.fileName)
        if (candidate.exists()) {
            openFile(context, candidate, task.mimeType)
            return
        }

        val pubCandidate = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), task.fileName)
        if (pubCandidate.exists()) {
            openFile(context, pubCandidate, task.mimeType)
            return
        }

        Toast.makeText(context, "File not found or still downloading", Toast.LENGTH_SHORT).show()
    }

    fun openFile(context: Context, file: File, explicitMimeType: String? = null) {
        try {
            val mimeType = explicitMimeType ?: resolveMimeType(file.name)
            val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No app found to open this file", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error opening file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTrackingProgress(context: Context) {
        if (trackerJob?.isActive == true) return

        trackerJob = scope.launch {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager ?: return@launch

            while (isActive) {
                val currentTasks = _activeDownloads.value
                if (currentTasks.isEmpty()) break

                var hasActive = false
                val updatedTasks = currentTasks.map { task ->
                    if (task.status == DownloadStatus.SUCCESSFUL || task.status == DownloadStatus.FAILED) {
                        task
                    } else {
                        val query = DownloadManager.Query().setFilterById(task.id)
                        val cursor: Cursor? = downloadManager.query(query)
                        if (cursor != null && cursor.moveToFirst()) {
                            val bytesDownloaded = cursor.getLong(
                                cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                            )
                            val totalBytes = cursor.getLong(
                                cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                            )
                            val statusInt = cursor.getInt(
                                cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
                            )
                            val localUriStr = try {
                                cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
                            } catch (e: Exception) {
                                null
                            }

                            val progress = if (totalBytes > 0) {
                                ((bytesDownloaded * 100) / totalBytes).toInt().coerceIn(0, 100)
                            } else {
                                0
                            }

                            val status = when (statusInt) {
                                DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.SUCCESSFUL
                                DownloadManager.STATUS_FAILED -> DownloadStatus.FAILED
                                DownloadManager.STATUS_PAUSED -> DownloadStatus.PAUSED
                                DownloadManager.STATUS_PENDING -> DownloadStatus.PENDING
                                else -> {
                                    hasActive = true
                                    DownloadStatus.RUNNING
                                }
                            }

                            val speedText = if (status == DownloadStatus.RUNNING) {
                                "${formatFileSize(bytesDownloaded)} / ${if (totalBytes > 0) formatFileSize(totalBytes) else "..."}"
                            } else if (status == DownloadStatus.SUCCESSFUL) {
                                "Complete • ${formatFileSize(if (totalBytes > 0) totalBytes else bytesDownloaded)}"
                            } else if (status == DownloadStatus.FAILED) {
                                "Download failed"
                            } else {
                                "Connecting..."
                            }

                            cursor.close()
                            task.copy(
                                downloadedBytes = bytesDownloaded,
                                totalBytes = if (totalBytes > 0) totalBytes else task.totalBytes,
                                progress = progress,
                                status = status,
                                speedText = speedText,
                                localUri = localUriStr
                            )
                        } else {
                            cursor?.close()
                            task
                        }
                    }
                }

                _activeDownloads.value = updatedTasks

                if (!hasActive) {
                    // Check again after longer interval or exit if all settled
                    val anyRunning = updatedTasks.any { it.status == DownloadStatus.RUNNING || it.status == DownloadStatus.PENDING }
                    if (!anyRunning) {
                        break
                    }
                }

                delay(800)
            }
        }
    }

    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        return when {
            gb >= 1.0 -> String.format("%.2f GB", gb)
            mb >= 1.0 -> String.format("%.1f MB", mb)
            kb >= 1.0 -> String.format("%.0f KB", kb)
            else -> "$bytes B"
        }
    }

    fun resolveMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: when (extension) {
            "apk" -> "application/vnd.android.package-archive"
            "pdf" -> "application/pdf"
            "zip" -> "application/zip"
            "rar" -> "application/x-rar-compressed"
            "7z" -> "application/x-7z-compressed"
            "mp4" -> "video/mp4"
            "mkv" -> "video/x-matroska"
            "mp3" -> "audio/mpeg"
            "wav" -> "audio/wav"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            "txt" -> "text/plain"
            "html" -> "text/html"
            else -> "application/octet-stream"
        }
    }
}

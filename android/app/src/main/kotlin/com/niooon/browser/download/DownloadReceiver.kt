package com.niooon.browser.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.widget.Toast

class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return

        if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (downloadId != -1L) {
                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager ?: return
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor: Cursor? = downloadManager.query(query)
                if (cursor != null && cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE))

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Toast.makeText(context, "Download complete: $title", Toast.LENGTH_LONG).show()
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Toast.makeText(context, "Download failed: $title", Toast.LENGTH_SHORT).show()
                    }
                    cursor.close()
                }
            }
        } else if (action == DownloadManager.ACTION_NOTIFICATION_CLICKED) {
            // Open Downloads screen in app or view downloads
            val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                putExtra("screen", "downloads")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }
    }
}

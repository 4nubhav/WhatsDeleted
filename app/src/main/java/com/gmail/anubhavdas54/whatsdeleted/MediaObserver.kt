package com.gmail.anubhavdas54.whatsdeleted

import android.os.Environment
import android.os.FileObserver
import android.util.Log
import java.io.File

private const val TAG = "MediaObserver"

class MediaObserver : FileObserver(File(Environment.getExternalStorageDirectory(),
    "WhatsApp${File.separator}Media${File.separator}WhatsApp Images").toString(), ALL_EVENTS) {

    override fun onEvent(event: Int, path: String?) {

        if (event == MOVED_TO) {
            try {
                val srcFile = File(Environment.getExternalStorageDirectory(),
                    "WhatsApp${File.separator}Media${File.separator}WhatsApp Images${File.separator}$path")
                val destFile = File(Environment.getExternalStorageDirectory(),
                    "WhatsDeleted${File.separator}WhatsDeleted Images${File.separator}$path")
                srcFile.copyTo(target = destFile, overwrite = false, bufferSize = DEFAULT_BUFFER_SIZE)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }
}

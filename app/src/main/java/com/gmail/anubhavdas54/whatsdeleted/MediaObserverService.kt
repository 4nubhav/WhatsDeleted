package com.gmail.anubhavdas54.whatsdeleted

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MediaObserverService : Service() {

    private val mediaObserver = MediaObserver()

    override fun onCreate() {
        super.onCreate()

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java)
            .let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = NotificationCompat.Builder(this,
            "mediaObserver")
            .setContentTitle("Media Observer")
            .setContentText("Watching for new images")
            .setSmallIcon(R.drawable.ic_delete)
            .setContentIntent(pendingIntent)
            .setTicker("Watching for new images")
            .build()

        startForeground(1337, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaObserver.startWatching()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaObserver.stopWatching()
    }
}

package com.gmail.anubhavdas54.whatsdeleted

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import java.io.File
import java.text.DateFormat
import java.util.*

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {

        if (sbn?.packageName == "com.whatsapp") {

            val date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date())
            val sender = sbn.notification.extras.getString("android.title")
            val msg = sbn.notification.extras.getString("android.text")

            File(this.filesDir, "msgLog.txt").appendText("$date | $sender: $msg\n")

        } else if (sbn?.packageName == "org.thoughtcrime.securesms") {

            val date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date())
            val sender = sbn.notification.extras.getString("android.title")
            val msg = sbn.notification.extras.getCharSequence("android.text")?.toString()

            File(this.filesDir, "signalMsgLog.txt").appendText("$date | $sender: $msg\n")
        }
    }
}

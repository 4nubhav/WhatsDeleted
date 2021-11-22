package com.gmail.anubhavdas54.whatsdeleted

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import java.io.File
import java.text.DateFormat
import java.util.*
// TODO: 09-Sep-21 use sqlite database(Room) for message storage
class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        // TODO: 09-Sep-21 is the if else if really neccesary?
        if (sbn?.packageName == "com.whatsapp") {

            val date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date())
            val sender = sbn.notification.extras.getString("android.title")
            val msg = sbn.notification.extras.getString("android.text")
            //log only when message does not contain "Checking for new messages, solves issue #26
            //count message example is 192 messages from 10 chats
            if(Utilities.notCheckingMsg(msg) || Utilities.notCountMsg(msg)) {
                File(this.filesDir, "msgLog.txt").appendText("$date | $sender: $msg\n")
            }
        } else if (sbn?.packageName == "org.thoughtcrime.securesms") {

            val date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date())
            val sender = sbn.notification.extras.getString("android.title")
            val msg = sbn.notification.extras.getCharSequence("android.text")?.toString()
            if(Utilities.notCheckingMsg(msg) || Utilities.notCountMsg(msg)) {
                File(this.filesDir, "signalMsgLog.txt").appendText("$date | $sender: $msg\n")
            }
        }
    }
}

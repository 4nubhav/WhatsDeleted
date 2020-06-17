package com.gmail.anubhavdas54.whatsdeleted

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class AlertDialogHelper {
    companion object {
        fun showDialog(context: Context,
                       title: String,
                       msg: String,
                       positiveBtnText: String, negativeBtnText: String?,
                       positiveBtnClickListener: DialogInterface.OnClickListener): AlertDialog {
            val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(positiveBtnText, positiveBtnClickListener)
            if (negativeBtnText != null)
                builder.setNegativeButton(negativeBtnText) { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
            return alert
        }
    }
}
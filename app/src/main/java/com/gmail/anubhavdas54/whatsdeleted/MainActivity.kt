package com.gmail.anubhavdas54.whatsdeleted

import android.Manifest
import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.PrintWriter
import java.lang.Exception
import android.content.DialogInterface
import android.widget.*
import androidx.appcompat.app.AlertDialog

private const val TAG = "MainActivity"
private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0

class MainActivity : AppCompatActivity() {

    private val whatsDeleted = File(Environment.getExternalStorageDirectory(),
        "WhatsDeleted${File.separator}WhatsDeleted Images")
    private val msgLog = File(Environment.getExternalStorageDirectory(), 
        "WhatsDeleted${File.separator}msgLog.txt")

    private val checkEmoji = String(Character.toChars(0x2714))
    private val crossEmoji = String(Character.toChars(0x274C))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Widgets
        val msgLogStatus = findViewById<TextView>(R.id.msg_log_status)
        val imgDirStatus = findViewById<TextView>(R.id.img_dir_status)
        val msgLogClrBtn = findViewById<Button>(R.id.msg_log_clr_btn)
        val imgDirDelBtn = findViewById<Button>(R.id.img_dir_del_btn)
        val medObsSwitch = findViewById<Switch>(R.id.med_obs_switch)
        val notificationListenerSwitch = findViewById<Switch>(R.id.notification_listener_switch)
        val test = findViewById<LinearLayout>(R.id.test)

        // TextView
        msgLogStatus.text = getString(R.string.msg_log_status_str,
            if (msgLog.exists()) checkEmoji else crossEmoji)
        imgDirStatus.text = getString(R.string.img_dir_status_str,
            if (msgLog.exists()) checkEmoji else crossEmoji)

        // Button
        msgLogClrBtn.setOnClickListener {
            showDialog(
                this@MainActivity,
                getString(R.string.clear_msg_log),
                getString(R.string.clear_msg_log_confirm),
                getString(R.string.yes),
                getString(R.string.cancel),
                DialogInterface.OnClickListener { _, _ ->
                    try {
                        PrintWriter(msgLog).use { out -> out.println("") }
                        Toast.makeText(applicationContext, getString(R.string.cleared), Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e(TAG, e.toString())
                        Toast.makeText(applicationContext, getString(R.string.clear_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        imgDirDelBtn.setOnClickListener{
            showDialog(
                this@MainActivity,
                getString(R.string.del_backup_img),
                getString(R.string.del_backup_img_confirm),
                getString(R.string.yes),
                getString(R.string.cancel),
                DialogInterface.OnClickListener { _, _ ->
                    try {
                        deleteRecursive(whatsDeleted)
                    } catch (e: Exception) {
                        Log.e(TAG, e.toString())
                        Toast.makeText(applicationContext, getString(R.string.del_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        // Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createNotificationChannel(
                "mediaObserver",
                "Media Observer",
                "Watches the default WhatsApp directories for new media",
                NotificationManager.IMPORTANCE_LOW
            )
        } else {
            @Suppress("DEPRECATION")
            createNotificationChannel(
                "mediaObserver",
                "Media Observer",
                "Watches the default WhatsApp directories for new media",
                Notification.PRIORITY_LOW
            )
        }

        // Request Storage Permission
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)

        // Media Observer Service
        val mediaObserverService = Intent(this, MediaObserverService::class.java)
        startService(mediaObserverService)

        medObsSwitch.isChecked = isServiceRunning(MediaObserverService::class.java)
        medObsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(mediaObserverService)
                Toast.makeText(applicationContext, getString(R.string.started), Toast.LENGTH_SHORT).show()
            }
            else {
                stopService(mediaObserverService)
                Toast.makeText(applicationContext, getString(R.string.stopped), Toast.LENGTH_SHORT).show()
            }
        }

        notificationListenerSwitch.isChecked = isServiceRunning(NotificationListener::class.java)
        notificationListenerSwitch.isClickable = false
        test.setOnClickListener {
            if (notificationListenerSwitch.isChecked) {
                showDialog(
                    this@MainActivity,
                    "Turn off",
                    "Settings > Apps  & notifications > Special app access > " +
                            "Notification Access > WhatsDeleted > Turn Off",
                    getString(R.string.ok),
                    null,
                    DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() }
                )
            }
            else {
                showDialog(
                    this@MainActivity,
                    "Turn on",
                    "Settings > Apps & notifications > Special app access > " +
                            "Notification Access > WhatsDeleted > Allow",
                    getString(R.string.ok),
                    null,
                    DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() }
                )
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun createNotificationChannel(id: String, name: String, desc: String, importance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(id, name, importance)
            mChannel.description = desc
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun createBackups() {
        if (!whatsDeleted.exists()) {
            if (!whatsDeleted.mkdirs())
                Toast.makeText(applicationContext, getString(R.string.create_backup_dir_failed),
                    Toast.LENGTH_SHORT).show()
        }
        if (!msgLog.exists()) {
            if (!msgLog.createNewFile())
                Toast.makeText(applicationContext, getString(R.string.create_msg_log_failed),
                    Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("SameParameterValue")
    private fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity,
                permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(permission), requestCode)
        } else {
            createBackups()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(applicationContext, getString(R.string.create_backup_dir), Toast.LENGTH_SHORT).show()
                createBackups()
            } else {
                Toast.makeText(applicationContext, getString(R.string.allow_storage_permission_msg), Toast.LENGTH_LONG).show()
            }
            return
        }
    }

    private fun showDialog(context: Context,
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

    @Suppress("DEPRECATION")
    private fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
        return (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == service.name }
    }

    private fun deleteRecursive(f: File) {
        if (f.isDirectory) {
            for (child in f.listFiles()) {
                if (!child.deleteRecursively())
                    Toast.makeText(applicationContext,
                        getString(R.string.unable_to_delete, child.toString()),
                        Toast.LENGTH_SHORT).show()
            }
        }
    }
}

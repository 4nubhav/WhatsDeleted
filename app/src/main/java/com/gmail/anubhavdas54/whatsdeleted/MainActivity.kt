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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.lang.Exception
import android.content.res.Configuration
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0

class MainActivity : AppCompatActivity() {

    private val msgLogFileName = "msgLog.txt"
    private val signalMsgLogFileName = "signalMsgLog.txt"
    private val whatsDeleted = File(Environment.getExternalStorageDirectory(),
        "WhatsDeleted${File.separator}WhatsDeleted Images")

    private val checkEmoji = String(Character.toChars(0x2714))
    private val crossEmoji = String(Character.toChars(0x274C))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Widgets
        val msgLogStatus = findViewById<TextView>(R.id.msg_log_status)
        val imgDirStatus = findViewById<TextView>(R.id.img_dir_status)
        val viewWALogBtn = findViewById<Button>(R.id.view_wa_log_btn)
        val viewSignalLogBtn = findViewById<Button>(R.id.view_signal_log_btn)
        val imgDirDelBtn = findViewById<Button>(R.id.img_dir_del_btn)
        val medObsSwitch = findViewById<SwitchMaterial>(R.id.med_obs_switch)
        val notificationListenerSwitch = findViewById<SwitchMaterial>(R.id.notification_listener_switch)
        val test = findViewById<LinearLayout>(R.id.test)

        // TextView
        msgLogStatus.text = getString(R.string.msg_log_status_str,
            if (File(this.filesDir, msgLogFileName).exists()
                && File(this.filesDir, signalMsgLogFileName).exists()) checkEmoji else crossEmoji)
        imgDirStatus.text = getString(R.string.img_dir_status_str,
            if (whatsDeleted.exists()) checkEmoji else crossEmoji)

        // Button
        // DRY
        viewWALogBtn.setOnClickListener {
            val intent = Intent(this, MsgLogViewerActivity::class.java)
            intent.putExtra("app", "whatsapp")
            startActivity(intent)
        }

        viewSignalLogBtn.setOnClickListener {
            val intent = Intent(this, MsgLogViewerActivity::class.java)
            intent.putExtra("app", "signal")
            startActivity(intent)
        }

        imgDirDelBtn.setOnClickListener{
            AlertDialogHelper.showDialog(
                this@MainActivity,
                getString(R.string.del_backup_img),
                getString(R.string.del_backup_img_confirm),
                getString(R.string.yes),
                getString(R.string.cancel)
            ) { _, _ ->
                try {
                    deleteRecursive(whatsDeleted)
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.del_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
                AlertDialogHelper.showDialog(
                    this@MainActivity,
                    "Turn off",
                    "Settings > Apps  & notifications > Special app access > " +
                            "Notification Access > WhatsDeleted > Turn Off",
                    getString(R.string.ok),
                    null
                ) { dialog, _ -> dialog.cancel() }
            }
            else {
                AlertDialogHelper.showDialog(
                    this@MainActivity,
                    "Turn on",
                    "Settings > Apps & notifications > Special app access > " +
                            "Notification Access > WhatsDeleted > Allow",
                    getString(R.string.ok),
                    null
                ) { dialog, _ -> dialog.cancel() }
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
        if (!File(this.filesDir, msgLogFileName).exists()) {
            if (!File(this.filesDir, msgLogFileName).createNewFile())
                Toast.makeText(applicationContext, getString(R.string.create_msg_log_failed),
                    Toast.LENGTH_SHORT).show()
        }

        if (!File(this.filesDir, signalMsgLogFileName).exists()) {
            if (!File(this.filesDir, signalMsgLogFileName).createNewFile())
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_toggle_theme -> {

                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

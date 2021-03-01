package com.gmail.anubhavdas54.whatsdeleted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_msg_log_viewer.*
import java.io.File
import java.io.PrintWriter
import java.lang.Exception

class MsgLogViewerActivity : AppCompatActivity() {

    private var msgLogFileName = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg_log_viewer)

        msgLogFileName =
            if (intent.getStringExtra("app") == "whatsapp") "msgLog.txt" else "signalMsgLog.txt"

        msg_log_recycler_view.adapter = MsgLogAdapter(readFile(File(this.filesDir, msgLogFileName)))
        msg_log_recycler_view.layoutManager = LinearLayoutManager(this)
        msg_log_recycler_view.setHasFixedSize(true)

        swipe_refresh_layout.setOnRefreshListener {
            refreshMsgLog()
            swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun readFile(fileName: File): List<String> = fileName.bufferedReader().readLines().asReversed()

    private fun refreshMsgLog() {
        msg_log_recycler_view.adapter = MsgLogAdapter(readFile(File(this.filesDir, msgLogFileName)))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_msg_log_viewer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_refresh -> {
                refreshMsgLog()
                return true
            }
            R.id.action_clear -> {
                AlertDialogHelper.showDialog(
                    this@MsgLogViewerActivity,
                    getString(R.string.clear_msg_log),
                    getString(R.string.clear_msg_log_confirm),
                    getString(R.string.yes),
                    getString(R.string.cancel)
                ) { _, _ ->
                    try {
                        PrintWriter(
                            File(
                                this.filesDir,
                                msgLogFileName
                            )
                        ).use { out -> out.println("") }
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.cleared),
                            Toast.LENGTH_SHORT
                        ).show()
                        refreshMsgLog()
                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.clear_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
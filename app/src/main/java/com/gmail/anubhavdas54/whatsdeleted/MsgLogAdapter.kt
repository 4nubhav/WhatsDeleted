package com.gmail.anubhavdas54.whatsdeleted

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.msg_log_item.view.*

class MsgLogAdapter(private val msgList: List<String>) : RecyclerView.Adapter<MsgLogAdapter.MsgLogViewHolder>() {

    class MsgLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msgTextView: TextView = itemView.msg_text_view
        val msgUsernameView:TextView = itemView.msg_username_view
        val msgDateView:TextView = itemView.msg_date_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgLogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.msg_log_item, parent, false)
        return MsgLogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MsgLogViewHolder, position: Int) {
        val msg = msgList[position]
        //this will ignore already added "checking for new messages"
        if(Utilities.notCheckingMsg(msg) || Utilities.notCountMsg(msg)) {
            val date = msg.substringBefore('|', "")
            val username = msg.substringAfter('|', "").substringBefore(':')
            val data = msg.substringAfter("|").substringAfter(':')
            holder.msgTextView.text = data
            holder.msgDateView.text = date
            holder.msgUsernameView.text = username
        }
    }

    override fun getItemCount(): Int = msgList.size
}
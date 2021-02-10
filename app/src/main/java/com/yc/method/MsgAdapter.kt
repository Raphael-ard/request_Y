package com.yc.method

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yc.R
import com.yc.basepackage.MyApplication

@SuppressLint("ServiceCast")
class MsgAdapter(val context: Context, val MsgList:List<Msg>) : RecyclerView.Adapter<MsgAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val msgcontent: TextView = view.findViewById(R.id.msgcontenet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.msg,parent,false)
        //处理点击事件
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            //复制到剪贴板
            val clipborad = MyApplication.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Content",holder.msgcontent.text.toString())
            clipborad.setPrimaryClip(clipData)
        }
        return holder
    }

    override fun getItemCount() = MsgList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result_msg = MsgList[position]
        holder.msgcontent.text = result_msg.msg_test
    }
}
package com.britomartis.android.britobudget.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.britomartis.android.britobudget.R
import com.britomartis.android.britobudget.models.Message
import com.britomartis.android.britobudget.utils.MessageType

class ChatAdapter(val context: Context, var dataset: List<Message>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when (viewType) {
            MessageType.USER_MESSAGE.ordinal -> {
                LayoutInflater.from(context).inflate(R.layout.layout_recyclerviewitem_user, parent, false)
            }
            MessageType.BOT_MESSAGE.ordinal -> {
                LayoutInflater.from(context).inflate(R.layout.layout_recyclerviewitem_bot, parent, false)
            }
            else -> throw IllegalStateException("Chat message ViewType not known")
        }

        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return dataset[position].messageType.ordinal
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = dataset[position]
        holder.messageContent.text = message.messageContent
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageContent = view.findViewById<TextView>(R.id.messageContent)
    }
}
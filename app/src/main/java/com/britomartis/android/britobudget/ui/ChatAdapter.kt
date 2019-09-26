package com.britomartis.android.britobudget.ui

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.britomartis.android.britobudget.R
import com.britomartis.android.britobudget.data.Message
import com.britomartis.android.britobudget.utils.MESSAGE_TYPE_BOT
import com.britomartis.android.britobudget.utils.MESSAGE_TYPE_USER
import com.britomartis.android.britobudget.utils.convertTimeLongToString
import com.britomartis.android.britobudget.utils.isOver5Minutes

class ChatAdapter(val context: Context, var dataset: List<Message>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when (viewType) {
            MESSAGE_TYPE_USER.hashCode() -> {
                LayoutInflater.from(context).inflate(R.layout.layout_recyclerviewitem_user, parent, false)
            }
            MESSAGE_TYPE_BOT.hashCode() -> {
                LayoutInflater.from(context).inflate(R.layout.layout_recyclerviewitem_bot, parent, false)
            }
            else -> throw IllegalStateException("Chat message ViewType not known")
        }

        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return dataset[position].messageType.hashCode()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = dataset[position]
        holder.messageContent.text = message.messageContent
        holder.messageTime.text = convertTimeLongToString(message.messageTime)

        if (message.messageType == MESSAGE_TYPE_BOT) {
            if (TextUtils.isEmpty(message.messageContent)) {
                if (isOver5Minutes(message.messageTime)) {
                    // Could not load the message
                    holder.messageContent.text = context.getString(R.string.no_network)
                    holder.lottieProgress.visibility = View.GONE
                    return
                }
                // Start Lottie Animation
                if (holder.lottieProgress != null) {
                    holder.lottieProgress.visibility = View.VISIBLE
                }
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageContent = view.findViewById<TextView>(R.id.messageContent)
        val messageTime = view.findViewById<TextView>(R.id.messageTime)
        val lottieProgress = view.findViewById<LottieAnimationView>(R.id.lottie_progress)
    }
}
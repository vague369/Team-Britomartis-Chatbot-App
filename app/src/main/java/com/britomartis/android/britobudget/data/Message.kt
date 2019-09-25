package com.britomartis.android.britobudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    val messageType: String,
    val messageTime: Long,
    val messageContent: String,
    @PrimaryKey(autoGenerate = true)
    var messageId: Int = 0
)
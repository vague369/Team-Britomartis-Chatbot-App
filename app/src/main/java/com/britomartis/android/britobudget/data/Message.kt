package com.britomartis.android.britobudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.britomartis.android.britobudget.utils.getRandomUUID

@Entity(tableName = "messages")
data class Message(
    val messageType: String,
    var messageTime: Long,
    var messageContent: String,
    @PrimaryKey
    val messageId: String = getRandomUUID()
)
package com.britomartis.android.britobudget.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(@Embedded val messageType: MessageType, @PrimaryKey val messageTime: Long, val messageContent: String)
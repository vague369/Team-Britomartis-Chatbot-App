package com.britomartis.android.britobudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(val messageType: String, @PrimaryKey val messageTime: Long, val messageContent: String)
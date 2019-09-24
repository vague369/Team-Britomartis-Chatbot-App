package com.britomartis.android.britobudget.models

import com.britomartis.android.britobudget.utils.MessageType

data class Message(val messageType: MessageType, val messageTime: String, val messageContent: String)
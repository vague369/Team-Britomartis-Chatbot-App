package com.britomartis.android.britobudget.data

import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient

class MessageRepository private constructor(private val messageDao: MessageDao) {

    lateinit var sessionsClient: SessionsClient
    lateinit var session: SessionName

    fun initChatBot(session: SessionName, sessionsClient: SessionsClient) {
        this.session = session
        this.sessionsClient = sessionsClient
    }

    fun getMessages() = messageDao.getMessages()

    suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message)
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: MessageRepository? = null

        fun getInstance(messageDao: MessageDao) =
            instance ?: synchronized(this) {
                instance ?: MessageRepository(messageDao).also { instance = it }
            }
    }
}
package com.britomartis.android.britobudget.data

import com.google.cloud.dialogflow.v2.*

class MessageRepository private constructor(private val messageDao: MessageDao) {

    lateinit var sessionsClient: SessionsClient
    lateinit var session: SessionName

    fun initChatbot(session: SessionName, sessionsClient: SessionsClient) {
        this.session = session
        this.sessionsClient = sessionsClient
    }

    suspend fun getChatbotReply(query: String): String? {
        try {
            val textInput = TextInput.newBuilder().setText(query).setLanguageCode("en-US").build()
            val queryInput = QueryInput.newBuilder().setText(textInput).build()
            val detectIntentRequest = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build()

            val response = sessionsClient.detectIntent(detectIntentRequest)
            return response.queryResult.fulfillmentText

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getMessages() = messageDao.getMessages()

    suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message)
    }

    suspend fun updateMessageContent(messageId: String, messageContent: String) {
        messageDao.updateMessageContent(messageId, messageContent)
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
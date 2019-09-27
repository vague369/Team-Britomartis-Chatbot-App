package com.britomartis.android.britobudget.data

import android.util.Log
import com.google.cloud.dialogflow.v2.*
import com.google.protobuf.Value

class MessageRepository private constructor(private val messageDao: MessageDao) {

    lateinit var sessionsClient: SessionsClient
    lateinit var session: SessionName

    fun initChatbot(session: SessionName, sessionsClient: SessionsClient) {
        this.session = session
        this.sessionsClient = sessionsClient
    }

    suspend fun getChatbotReply(query: String): Pair<String?, Map<String, Value>?>? {
        try {
            val textInput = TextInput.newBuilder().setText(query).setLanguageCode("en-US").build()
            val queryInput = QueryInput.newBuilder().setText(textInput).build()
            val detectIntentRequest = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build()
            val response = sessionsClient.detectIntent(detectIntentRequest)

            var payload: Map<String, Value>? = null
            if (response.queryResult.fulfillmentMessagesCount > 1) {
                // There's a payload
                payload = response.queryResult.getFulfillmentMessages(response.queryResult.fulfillmentMessagesCount - 1)
                    .payload
                    .fieldsMap //.getValue("type").stringValue
            }

            return Pair<String?, Map<String, Value>?>(response.queryResult.fulfillmentText, payload)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, e.toString())
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
        const val TAG = "MainActivityViewModel"

        // For Singleton instantiation
        @Volatile
        private var instance: MessageRepository? = null

        fun getInstance(messageDao: MessageDao) =
            instance ?: synchronized(this) {
                instance ?: MessageRepository(messageDao).also { instance = it }
            }
    }
}
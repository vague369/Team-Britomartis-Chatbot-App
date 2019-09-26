package com.britomartis.android.britobudget.viewmodels

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.britomartis.android.britobudget.R
import com.britomartis.android.britobudget.data.Message
import com.britomartis.android.britobudget.data.MessageRepository
import com.britomartis.android.britobudget.utils.*
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.google.cloud.dialogflow.v2.SessionsSettings
import kotlinx.coroutines.launch

class MainActivityViewModel(val context: Context, private val messageRepository: MessageRepository) : ViewModel() {

    val messageLiveList: LiveData<List<Message>>
        get() = messageRepository.getMessages()

    private val sessionUUID = getRandomUUID()

    init {
        // InitV2Chatbot
        try {
            val stream = context.resources.openRawResource(R.raw.britobudget_eiyfbe_38f24d45f456)
            val credentials = GoogleCredentials.fromStream(stream)
            val projectIdStr = (credentials as ServiceAccountCredentials).projectId

            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionSettings =
                settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build()
            val sessionsClient = SessionsClient.create(sessionSettings)
            val session = SessionName.of(projectIdStr, sessionUUID)

            messageRepository.initChatbot(session, sessionsClient)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendButtonClicked(inputText: String?) {
        if (inputText == null) return
        if (TextUtils.isEmpty(inputText.trim())) return

        val userMessage = Message(
            MESSAGE_TYPE_USER,
            getCurrentTimeAsLong(),
            inputText.trim()
        )
        val botResponse = Message(
            MESSAGE_TYPE_BOT,
            getCurrentTimeAsLong(),
            ""
        )

        viewModelScope.launch {
            messageRepository.insertMessage(userMessage)
            messageRepository.insertMessage(botResponse)

            if (hasConnectivity(context)) {
                Log.d(TAG, "Has connectivity")
                /*val reply = messageRepository.getChatbotReply(inputText.trim())
                botResponse.messageContent = reply
                messageRepository.updateMessage(botResponse)*/
            } else {
                Log.d(TAG, "No connectivity")
                // If the network is down, reply with an error message
                botResponse.messageContent = context.getString(R.string.no_network)
                messageRepository.updateMessageContent(botResponse.messageId, botResponse.messageContent)
                Log.d(TAG, botResponse.messageContent)
            }
        }
    }

    companion object {
        const val TAG = "MainActivityViewModel"
    }
}
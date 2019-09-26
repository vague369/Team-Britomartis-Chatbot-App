package com.britomartis.android.britobudget.viewmodels

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.britomartis.android.britobudget.R
import com.britomartis.android.britobudget.data.Message
import com.britomartis.android.britobudget.data.MessageRepository
import com.britomartis.android.britobudget.utils.MESSAGE_TYPE_BOT
import com.britomartis.android.britobudget.utils.MESSAGE_TYPE_USER
import com.britomartis.android.britobudget.utils.getCurrentTimeAsLong
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.google.cloud.dialogflow.v2.SessionsSettings
import kotlinx.coroutines.launch
import java.util.*

class MainActivityViewModel(context: Context, private val messageRepository: MessageRepository) : ViewModel() {

    val messageLiveList: LiveData<List<Message>>
        get() = messageRepository.getMessages()

    private val sessionUUID = UUID.randomUUID().toString()

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

            messageRepository.initChatBot(session, sessionsClient)

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
        }
    }

    companion object {
        const val TAG = "MainActivityViewModel"
    }
}
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
import com.google.protobuf.Value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun sayFirstHello() {
        // There's no message history. Say Hi
        val botResponse = Message(
            MESSAGE_TYPE_BOT,
            getCurrentTimeAsLong(),
            context.getString(R.string.chatbot_defaultreply_first_hello)
        )

        viewModelScope.launch {
            messageRepository.insertMessage(botResponse)
        }
    }

    fun sendButtonClicked(inputText: String?) {
        if (inputText == null) return
        val trimmedText = inputText.trim()
        if (TextUtils.isEmpty(trimmedText)) return

        // Build the user's message
        val userMessage = Message(
            MESSAGE_TYPE_USER,
            getCurrentTimeAsLong(),
            trimmedText
        )

        // Build an empty bot message
        val botResponse = Message(
            MESSAGE_TYPE_BOT,
            getCurrentTimeAsLong(),
            ""
        )

        // Coroutines bridge
        // TODO: Messy code, FIX
        viewModelScope.launch {
            // Dispatchers.Main
            messageRepository.insertMessage(userMessage)
            messageRepository.insertMessage(botResponse)

            // Get the chatbot's reply
            withContext(Dispatchers.IO) {
                // Dispatchers.IO
                val replyPair: Pair<String?, Map<String, Value>?>?
                var reply: String?
                if (hasConnectivity(context)) {
                    replyPair = messageRepository.getChatbotReply(trimmedText)
                    reply = replyPair?.first
                } else {
                    replyPair = null
                }

                Log.d(TAG, replyPair.toString())

                reply = parseBotReply(context, replyPair)
                botResponse.messageContent = when {
                    reply == null -> context.getString(R.string.no_network)
                    TextUtils.isEmpty(reply) -> context.getString(R.string.no_response_from_bot)
                    else -> reply
                }
                botResponse.messageTime = getCurrentTimeAsLong()

                // Check if the user has a saved name
                if ((reply != null) && !userNameAlreadySaved(context)) {
                    // There is no username saved insert a username question
                    val userNameQuery = Message(
                        MESSAGE_TYPE_BOT,
                        getCurrentTimeAsLong(),
                        context.getString(R.string.chatbot_defaultreply_what_is_your_name)
                    )
                    messageRepository.insertMessage(userNameQuery)
                }

                messageRepository.updateMessageContent(botResponse.messageId, botResponse.messageContent)
            }
        }
    }

    companion object {
        const val TAG = "MainActivityViewModel"
    }
}
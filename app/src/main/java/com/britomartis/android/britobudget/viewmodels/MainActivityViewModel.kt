package com.britomartis.android.britobudget.viewmodels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.britomartis.android.britobudget.data.Message
import com.britomartis.android.britobudget.data.MessageRepository
import com.britomartis.android.britobudget.utils.MESSAGE_TYPE_BOT
import com.britomartis.android.britobudget.utils.MESSAGE_TYPE_USER
import com.britomartis.android.britobudget.utils.getCurrentTimeAsLong
import kotlinx.coroutines.launch

class MainActivityViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    val messageLiveList: LiveData<List<Message>>
        get() = messageRepository.getMessages()

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
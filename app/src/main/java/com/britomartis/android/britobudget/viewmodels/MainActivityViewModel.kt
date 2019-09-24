package com.britomartis.android.britobudget.viewmodels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.britomartis.android.britobudget.models.Message
import com.britomartis.android.britobudget.utils.MessageType
import com.britomartis.android.britobudget.utils.getCurrentTimeAsString

class MainActivityViewModel : ViewModel() {
    // Keep a list of messages
    lateinit var _messageList: MutableLiveData<MutableList<Message>>
    val messageList: LiveData<MutableList<Message>> = _messageList

    init {
        _messageList.value = mutableListOf()
    }

    fun sendButtonClicked(inputText: String?) {
        if (inputText == null) return
        if (TextUtils.isEmpty(inputText)) return

        // Build a new message
        val userMessage = Message(MessageType.USER_MESSAGE, getCurrentTimeAsString(), inputText)
        _messageList.value?.add(userMessage)
    }

}
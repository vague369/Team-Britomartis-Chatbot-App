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
    private val messageList = mutableListOf<Message>()
    // Also keep an observable copy
    private val _messageLiveList = MutableLiveData<List<Message>>()
    val messageLiveList: LiveData<List<Message>>
        get() = _messageLiveList

    init {
        _messageLiveList.value = listOf()
    }

    fun sendButtonClicked(inputText: String?) {
        inputText?.apply { trim() }
        if (inputText == null) return
        if (TextUtils.isEmpty(inputText)) return

        val userMessage = Message(MessageType.USER_MESSAGE, getCurrentTimeAsString(), inputText)
        messageList.add(userMessage)

        _messageLiveList.value = messageList
    }

    companion object {
        const val TAG = "MainActivityViewModel"
    }
}
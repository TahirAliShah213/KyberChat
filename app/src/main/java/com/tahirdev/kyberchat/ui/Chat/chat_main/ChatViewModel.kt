package com.tahirdev.kyberchat.ui.Chat.chat_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Account Created"
    }
    val text: LiveData<String> = _text

    private val _text1 = MutableLiveData<String>().apply {
        value = "No chat yet.\n" +
                "Get started by messaging a friends."
    }
    val text1: LiveData<String> = _text1
}

data class Message(
    var content: String,
    val isSentByUser: Boolean,
    val timestamp: Long,
    var reaction: String? = null,
    val isVoiceNote: Boolean = false
)


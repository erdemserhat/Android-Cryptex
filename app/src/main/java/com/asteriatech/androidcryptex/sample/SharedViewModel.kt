package com.asteriatech.androidcryptex.sample

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var decodedText by mutableStateOf("")
    val password by mutableStateOf("")
    val dataToEncryptorDecrypt = "Sensitive Data"

    fun setMessage(newMessage: String) {
        decodedText = newMessage
    }

    fun getMessage(): String {
        //decryptdecodedtext(decodedText)
        return decodedText

    }







}
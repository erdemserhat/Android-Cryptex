package com.asteriatech.androidcryptex.sample

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var decodedText by mutableStateOf("")
    var password by mutableStateOf("")

    fun setMessage(newMessage: String) {
        decodedText = newMessage
    }

    fun setPasswordValue(newPassword: String) {
        password = newPassword
    }


    fun encrypt(encryptionData: String) {
        decodedText=CryptoUtils.encrypt(encryptionData, password)

    }

    fun decrypt(decryptionData: String) {
        decodedText= CryptoUtils.decrypt(decryptionData, password)

    }


}
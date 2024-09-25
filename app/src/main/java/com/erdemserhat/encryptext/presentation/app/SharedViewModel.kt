package com.erdemserhat.encryptext.presentation.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.erdemserhat.encryptext.encryption.CryptoCoreFunctions

/**
 * ViewModel for managing the state of encryption and decryption operations.
 * This class is responsible for holding the UI-related data and logic
 * for the encryption application.
 */
class SharedViewModel : ViewModel() {
    // Holds the decoded text resulting from encryption or decryption operations.
    var decodedText by mutableStateOf("")

    // Holds the password used for encryption or decryption.
    var password by mutableStateOf("")

    /**
     * Sets a new message to the decodedText.
     * @param newMessage The new message to set.
     */
    fun setMessage(newMessage: String) {
        decodedText = newMessage
    }

    /**
     * Sets a new password for encryption or decryption.
     * @param newPassword The new password to set.
     */
    fun setPasswordValue(newPassword: String) {
        password = newPassword
    }

    /**
     * Encrypts the provided data using the stored password.
     * @param encryptionData The data to be encrypted.
     */
    fun encrypt(encryptionData: String) {
        decodedText = CryptoCoreFunctions.encrypt(encryptionData, password)
    }

    /**
     * Decrypts the provided data using the stored password.
     * @param decryptionData The data to be decrypted.
     */
    fun decrypt(decryptionData: String) {
        decodedText = CryptoCoreFunctions.decrypt(decryptionData, password)
    }
}

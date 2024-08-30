package com.asteriatech.androidcryptex.sample

import android.annotation.SuppressLint
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object CryptoUtils {
    // Sadece Türkçe karakterler
    private val CHARACTERS = "abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ"

    private fun getShiftFromPassword(password: String): Int {
        // Şifreden kaydırma miktarını hesapla (ASCII değerlerinin toplamı)
        val shift = password.sumOf { it.code } % CHARACTERS.length
        return if (shift == 0) 1 else shift
    }

    private fun shiftChar(c: Char, shift: Int): Char {
        val index = CHARACTERS.indexOf(c)
        return if (index >= 0) {
            CHARACTERS[(index + shift + CHARACTERS.length) % CHARACTERS.length]
        } else {
            c // Karakter tanımlı değilse olduğu gibi döndür
        }
    }

    fun encrypt(plainText: String, password: String): String {
        val shift = getShiftFromPassword(password)
        return plainText.map { shiftChar(it, shift) }.joinToString("")
    }

    fun decrypt(encryptedText: String, password: String): String {
        val shift = getShiftFromPassword(password)
        return encryptedText.map { shiftChar(it, -shift) }.joinToString("")
    }
}
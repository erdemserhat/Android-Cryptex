package com.erdemserhat.encryptext.sample


object CryptoUtils {
    // Türkçe karakterler ve rakamlar
    private val CHARACTERS = "abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ0123456789"

    private fun getShiftFromPassword(password: String): Int {
        // Şifreden kaydırma miktarını hesapla (ASCII değerlerinin toplamı)
        val shift = password.sumOf { it.code } % CHARACTERS.length
        return if (shift == 0) 13 else shift
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

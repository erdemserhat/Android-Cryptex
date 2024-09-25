package com.erdemserhat.encryptext.encryption

/**
 * CryptoCoreFunctions is an object that provides basic encryption and decryption functions.
 *
 * The encryption algorithm uses a character shifting technique based on a provided password.
 * Characters from a predefined set are shifted by a value determined from the sum of the character
 * codes in the password. This simple method provides basic text obfuscation.
 *
 * You can extend this functionality by adding more complex encryption techniques or
 * additional helper functions to suit your needs.
 */
object CryptoCoreFunctions {

    // The set of characters that will be used for encryption. Any character outside this set remains unchanged.
    private val CHARACTERS = "abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ0123456789"

    /**
     * This function calculates a "shift" value from the provided password.
     * The shift is determined by summing up the Unicode values of each character in the password,
     * then taking the result modulo the length of the CHARACTERS array.
     * If the shift is zero, a default value of 13 is used.
     *
     * @param password the password used to generate the shift value
     * @return an integer value representing the shift
     */
    private fun getShiftFromPassword(password: String): Int {
        val shift = password.sumOf { it.code } % CHARACTERS.length
        return if (shift == 0) 13 else shift
    }

    /**
     * Shifts the given character by the specified number of positions within the CHARACTERS array.
     * If the character is not found in the CHARACTERS array, it is returned unchanged.
     *
     * @param c the character to be shifted
     * @param shift the number of positions to shift the character
     * @return the shifted character
     */
    private fun shiftChar(c: Char, shift: Int): Char {
        val index = CHARACTERS.indexOf(c)
        return if (index >= 0) {
            CHARACTERS[(index + shift + CHARACTERS.length) % CHARACTERS.length]
        } else {
            c // If the character is not in CHARACTERS, return it as is
        }
    }

    /**
     * Encrypts the given plain text using the provided password.
     * Each character in the plain text is shifted by the value derived from the password.
     *
     * @param plainText the original text to be encrypted
     * @param password the password used to determine the shift value for encryption
     * @return the encrypted text
     */
    fun encrypt(plainText: String, password: String): String {
        val shift = getShiftFromPassword(password)
        return plainText.map { shiftChar(it, shift) }.joinToString("")
    }

    /**
     * Decrypts the given encrypted text using the provided password.
     * The decryption process reverses the shift that was applied during encryption.
     *
     * @param encryptedText the text to be decrypted
     * @param password the password used to determine the shift value for decryption
     * @return the decrypted (original) text
     */
    fun decrypt(encryptedText: String, password: String): String {
        val shift = getShiftFromPassword(password)
        return encryptedText.map { shiftChar(it, -shift) }.joinToString("")
    }
}

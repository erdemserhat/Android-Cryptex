package com.erdemserhat.encryptext.presentation.app

/**
 * Sealed class representing different screens in the application.
 * This structure allows for type-safe navigation between composable screens.
 */
sealed class Screen(val route: String) {
    // Represents the Camera Scanner screen.
    // This screen will be used for capturing images or scanning QR codes.
    object CameraScannerScreen : Screen("camera_scanner_screen")

    // Represents the Scanner Result screen.
    // This screen displays the results after scanning or decrypting text.
    object ScannerResultScreen : Screen("scanner_result_screen")

    // Represents the Encryption screen.
    // This screen allows users to encrypt or decrypt text based on the selected operation.
    object EncryptionScreen : Screen("encryption_screen")
}

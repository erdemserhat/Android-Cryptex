package com.asteriatech.androidcryptex.sample

sealed class Screen(val route: String) {
    object CameraScannerScreen : Screen("camera_scanner_screen")
    object ScannerResultScreen : Screen("scanner_result_screen")
    object EncryptionScreen : Screen("encryption_screen")
}
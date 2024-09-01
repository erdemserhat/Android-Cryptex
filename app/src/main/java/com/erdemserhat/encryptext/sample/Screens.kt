package com.erdemserhat.encryptext.sample

sealed class Screen(val route: String) {
    object CameraScannerScreen : Screen("camera_scanner_screen")
    object ScannerResultScreen : Screen("scanner_result_screen")
    object EncryptionScreen : Screen("encryption_screen")
}
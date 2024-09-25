package com.erdemserhat.encryptext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.erdemserhat.encryptext.presentation.app.App

/**
 * Main activity of the Encryptext application.
 * This activity serves as the entry point of the app, setting up the splash screen and the main content.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install the splash screen to provide a smooth loading experience.
        installSplashScreen()

        // Set the content of the activity to the App composable.
        setContent {
            App()
        }
    }
}

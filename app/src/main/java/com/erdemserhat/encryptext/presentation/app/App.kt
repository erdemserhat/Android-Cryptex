package com.erdemserhat.encryptext.presentation.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erdemserhat.encryptext.R
import com.erdemserhat.encryptext.presentation.screens.camera_scanner.CameraScannerScreen
import com.erdemserhat.encryptext.presentation.screens.encryption.EncryptionScreen
import com.erdemserhat.encryptext.presentation.screens.result.ScannerResultScreen

/**
 * App is the main composable function that sets up the overall structure of the application
 * using Jetpack Compose. It creates a scaffold with a top bar and bottom navigation bar.
 * It also handles the navigation between different screens.
 *
 * The app uses a shared view model (`SharedViewModel`) that is passed across different screens
 * for managing shared states or data.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    // Creating a NavController for managing navigation between screens
    val navController = rememberNavController()

    // Using the shared ViewModel across screens
    val sharedViewModel: SharedViewModel = viewModel()

    // Getting the context to access string resources
    val context = LocalContext.current

    // State variable to dynamically update the title of the top bar
    var topBarTitle by remember { mutableStateOf(context.getString(R.string.encrypt_decrypt)) }

    /**
     * Scaffold is the layout that provides the structure for the app, including
     * a top bar and a bottom navigation bar. The content area is managed
     * by a NavHost, which switches between different screens based on navigation actions.
     */
    Scaffold(
        // Bottom navigation bar which allows switching between different screens
        bottomBar = {
            BottomNavBar(navController = navController, { topBarTitle = it }, context)
        },
        // Top app bar displaying the current screen's title
        topBar = {
            TopAppBar(title = { Text(text = topBarTitle) })
        }
    ) { innerPadding ->
        // NavHost manages navigation between different screens and handles padding.
        NavHost(
            navController = navController,
            startDestination = Screen.EncryptionScreen.route, // Starting screen
            Modifier.padding(innerPadding) // Padding for the inner content to avoid overlap with system UI
        ) {
            // Camera scanner screen route setup
            composable(Screen.CameraScannerScreen.route) {
                CameraScannerScreen(navController = navController, viewModel = sharedViewModel)
            }

            // Scanner result screen route setup
            composable(Screen.ScannerResultScreen.route) {
                ScannerResultScreen(navController = navController, viewModel = sharedViewModel)

                // This effect runs when the screen is loaded to update the top bar title
                LaunchedEffect(Unit) {
                    topBarTitle = context.getString(R.string.operation_result)
                }
            }

            // Encryption screen route setup
            composable(Screen.EncryptionScreen.route) {
                EncryptionScreen(navController = navController, viewModel = sharedViewModel)
            }
        }
    }
}

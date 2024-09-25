package com.erdemserhat.encryptext.presentation.app

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.erdemserhat.encryptext.R

/**
 * A composable function that defines the bottom navigation bar for the app.
 * The navigation bar contains icons for different screens, such as the Camera Scanner,
 * Scanner Result, and Encrypt/Decrypt screens.
 *
 * @param navController: Used for navigation between different composable screens.
 * @param onScreenChange: A callback function to change the top bar's title based on the selected screen.
 * @param context: Provides access to application resources like string resources for screen titles.
 */
@Composable
fun BottomNavBar(navController: NavController, onScreenChange: (String) -> Unit, context: Context) {

    // Observes the current backstack entry to determine the current screen route
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Current route is retrieved from the navBackStackEntry to highlight the selected tab
    val currentRoute = navBackStackEntry?.destination?.route

    /**
     * Creates the navigation bar with individual navigation items.
     * Each item navigates to a specific screen when clicked and updates the top bar's title.
     */
    NavigationBar {

        // Navigation item for the Camera Scanner screen
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(id = R.drawable.camera), contentDescription = "Camera Scanner")
            },
            selected = currentRoute == Screen.CameraScannerScreen.route, // Highlight when selected
            onClick = {
                // Navigate to CameraScannerScreen, maintaining a single top instance and restoring state
                navController.navigate(Screen.CameraScannerScreen.route) {
                    launchSingleTop = true // Ensures no duplicate screens are added
                    restoreState = true // Restores state when navigating back to the screen
                    onScreenChange(context.getString(R.string.camera_scanner)) // Updates top bar title
                }
            }
        )

        // Navigation item for the Scanner Result screen
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(id = R.drawable.result), contentDescription = "Scanner Result")
            },
            selected = currentRoute == Screen.ScannerResultScreen.route, // Highlight when selected
            onClick = {
                // Navigate to ScannerResultScreen, with the same settings as above
                navController.navigate(Screen.ScannerResultScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                    onScreenChange(context.getString(R.string.operation_result)) // Updates top bar title
                }
            }
        )

        // Navigation item for the Encrypt/Decrypt screen
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(id = R.drawable.typing), contentDescription = "Encrypt/Decrypt")
            },
            selected = currentRoute == Screen.EncryptionScreen.route, // Highlight when selected
            onClick = {
                // Navigate to EncryptionScreen, with the same settings as above
                navController.navigate(Screen.EncryptionScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                    onScreenChange(context.getString(R.string.encrypt_decrypt)) // Updates top bar title
                }
            }
        )
    }
}

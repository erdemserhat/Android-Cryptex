package com.erdemserhat.encryptext.sample

import android.content.Context
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.erdemserhat.encryptext.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    val context = LocalContext.current
    var topBarTitle by remember { mutableStateOf(context.getString(R.string.encrypt_decrypt)) }



    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController,{topBarTitle=it},context)
        },
        topBar = {
            TopAppBar(title = { Text(text = topBarTitle) })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.EncryptionScreen.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.CameraScannerScreen.route) {
                CameraScannerScreen(navController =navController, viewModel = sharedViewModel)
            }

            composable(Screen.ScannerResultScreen.route) {
                ScannerResultScreen(navController = navController, viewModel = sharedViewModel)
                LaunchedEffect(Unit) {
                    topBarTitle =context.getString(R.string.operation_result)
                }
              
            }
            composable(Screen.EncryptionScreen.route) {
                EncryptionScreen(navController = navController, viewModel = sharedViewModel)
            }
        }
    }
}
@Composable
fun BottomNavBar(navController: NavController,onScreenChange:(String)->Unit,context: Context) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.camera), contentDescription = "Camera Scanner") },
            //label = { Text("Camera Scanner") },
            selected = currentRoute == Screen.CameraScannerScreen.route,
            onClick = {
                navController.navigate(Screen.CameraScannerScreen.route) {
                    // Avoid multiple copies of the same destination
                    launchSingleTop = true
                    restoreState = true
                    onScreenChange(context.getString(R.string.camera_scanner))
                }
            }
        )
        NavigationBarItem(

            icon = { Icon(painter = painterResource(id = R.drawable.result), contentDescription = "Scanner Result") },
           // label = { Text("Scanner Result") },
            selected = currentRoute == Screen.ScannerResultScreen.route,
            onClick = {
                navController.navigate(Screen.ScannerResultScreen.route) {
                    // Avoid multiple copies of the same destination
                    launchSingleTop = true
                    restoreState = true
                    onScreenChange(context.getString(R.string.operation_result))
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.typing), contentDescription = "Encrypt/Decrypt") },
            //label = { Text("Encrypt/Decrypt") },
            selected = currentRoute == Screen.EncryptionScreen.route,
            onClick = {
                navController.navigate(Screen.EncryptionScreen.route) {
                    // Avoid multiple copies of the same destination
                    launchSingleTop = true
                    restoreState = true
                    onScreenChange(context.getString(R.string.encrypt_decrypt))
                }
            }
        )
    }
}

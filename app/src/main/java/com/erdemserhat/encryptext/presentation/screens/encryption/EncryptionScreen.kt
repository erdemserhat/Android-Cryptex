package com.erdemserhat.encryptext.presentation.screens.encryption

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.erdemserhat.encryptext.R
import com.erdemserhat.encryptext.presentation.app.Screen
import com.erdemserhat.encryptext.presentation.app.SharedViewModel

@Composable
fun EncryptionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SharedViewModel
) {
    // State variables to manage encryption/decryption mode and user inputs
    var isEncryptionEnabled by remember { mutableStateOf(true) }
    var isDecryptionEnabled by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Getting the context for accessing string resources
    val context = LocalContext.current

    // Main column layout for the screen
    Column(modifier = Modifier.fillMaxSize()) {
        // Header text
        Text(
            text = context.getString(R.string.operation_placeholder),
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )

        // Checkbox for Encryption mode
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = isEncryptionEnabled,
                onCheckedChange = {
                    isEncryptionEnabled = it
                    isDecryptionEnabled = !it // Toggle the other checkbox
                },
            )
            Text(text = context.getString(R.string.encryption), fontSize = 20.sp)
        }

        // Checkbox for Decryption mode
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = isDecryptionEnabled,
                onCheckedChange = {
                    isDecryptionEnabled = it
                    isEncryptionEnabled = !it // Toggle the other checkbox
                }
            )
            Text(text = context.getString(R.string.decrypt), fontSize = 20.sp)
        }

        // Password input field
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            placeholder = { Text(text = context.getString(R.string.password_placeholder)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                // Password visibility toggle icon
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val icon: Int = if(passwordVisible) R.drawable.visibility_eye_icon else R.drawable.visibility_off_eye_icon
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        // Spacer for visual separation
        Spacer(modifier = Modifier.size(20.dp))

        // Column for text input and action button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState())
        ) {
            // Text input for the text to encrypt/decrypt
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(250.dp),
                placeholder = { Text(text = context.getString(R.string.type_here)) }
            )

            // Button to trigger the encryption/decryption operation
            Button(onClick = {
                // Set the password value in the view model
                viewModel.setPasswordValue(password)
                // Perform encryption or decryption based on the selected mode
                if (isEncryptionEnabled) {
                    viewModel.encrypt(text)
                } else {
                    viewModel.decrypt(text)
                }
                // Navigate to the result screen
                navController.navigate(Screen.ScannerResultScreen.route)
            }) {
                Text(text = context.getString(R.string.operate))
            }
        }
    }
}

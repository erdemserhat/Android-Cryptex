package com.asteriatech.androidcryptex.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.asteriatech.androidcryptex.R
import androidx.compose.ui.res.painterResource

@Composable
fun EncryptionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SharedViewModel
) {
    var isEncryptionEnabled by remember { mutableStateOf(true) }
    var isDecryptionEnabled by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Select the operation you want to do",
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(

                checked = isEncryptionEnabled,
                onCheckedChange = {
                    isEncryptionEnabled = it
                    isDecryptionEnabled = !it

                },
            )

            Text(text = "Encryption", fontSize = 20.sp)

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = isDecryptionEnabled,
                onCheckedChange = {
                    isDecryptionEnabled = it
                    isEncryptionEnabled = !it
                }
            )

            Text(text = "Decryption", fontSize = 20.sp)

        }

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            placeholder = { Text(text = "Type a password (optional)...") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val icon: Int = if(passwordVisible) R.drawable.visibility_eye_icon else R.drawable.visibility_off_eye_icon
                    Icon(painter = painterResource(id = icon), contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            }
        )
        
        Spacer(modifier = Modifier.size(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(
                    rememberScrollState()
                ),


            ) {

            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(250.dp),
                placeholder = { Text(text = "Select your operation and type here...") }


            )

            Button(onClick = {
                viewModel.setPasswordValue(password)
                if (isEncryptionEnabled)
                    viewModel.encrypt(text)
                else
                    viewModel.decrypt(text)
                navController.navigate(Screen.ScannerResultScreen.route)

            }) {
                Text(text = "Operate")

            }


        }


    }

}



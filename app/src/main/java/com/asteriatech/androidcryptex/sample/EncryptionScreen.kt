package com.asteriatech.androidcryptex.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EncryptionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SharedViewModel
) {
    var isEncryptionEnabled by remember { mutableStateOf(true) }
    var isDecryptionEnabled by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

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
                    .height(250.dp)

            )
            
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Operate")
                
            }


        }


    }

}




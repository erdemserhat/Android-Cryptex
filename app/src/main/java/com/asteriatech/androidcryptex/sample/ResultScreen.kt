package com.asteriatech.androidcryptex.sample

import android.content.ClipboardManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.asteriatech.androidcryptex.R

@Composable
fun ScannerResultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SharedViewModel
) {
    // LocalClipboardManager, panoya erişmek için kullanılır
    val clipboardManager: androidx.compose.ui.platform.ClipboardManager =
        LocalClipboardManager.current
    val context = LocalContext.current

    Column(modifier = modifier.padding(20.dp)) {
        if (viewModel.decodedText.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Text(text = "There is no result yet...", color = Color.Black, fontSize = 20.sp)
                    Spacer(modifier = Modifier.size(50.dp))
                    Image(
                        painter = painterResource(id = R.drawable.empty_image),
                        contentDescription = null,


                        )

                }


            }


        } else {
            // Buton ekleme

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(8.dp),

                ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    // Text bileşeni
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState()) // İçeriğin kaydırılmasını sağlar
                    ) {
                        // Text bileşeni
                        Text(
                            text = viewModel.decodedText,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Panoya text'i kopyalama
                clipboardManager.setText(AnnotatedString(viewModel.decodedText))
                Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()


            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = "Copy Text")
            }
        }
    }
}


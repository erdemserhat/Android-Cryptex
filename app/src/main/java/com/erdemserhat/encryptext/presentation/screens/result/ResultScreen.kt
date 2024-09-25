package com.erdemserhat.encryptext.presentation.screens.result

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.erdemserhat.encryptext.R
import com.erdemserhat.encryptext.presentation.app.SharedViewModel

@Composable
fun ScannerResultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SharedViewModel
) {
    // LocalClipboardManager allows access to the clipboard
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    // Main layout with padding
    Column(modifier = modifier.padding(20.dp)) {
        // Check if decodedText is empty and display appropriate content
        if (viewModel.decodedText.isEmpty()) {
            // If no result, show a placeholder message and an image
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Text(
                        text = context.getString(R.string.no_result),
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.size(50.dp))
                    Image(
                        painter = painterResource(id = R.drawable.empty_image),
                        contentDescription = null
                    )
                }
            }
        } else {
            // If there's a result, display the decoded text in a card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(8.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    // Vertical scrollable column for the decoded text
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        // Display the decoded text
                        Text(
                            text = viewModel.decodedText,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Button to copy text to clipboard
            Button(onClick = {
                // Copy the decoded text to clipboard
                clipboardManager.setText(AnnotatedString(viewModel.decodedText))
                // Show a toast message indicating the text was copied
                Toast.makeText(context, context.getText(R.string.copy_text_info), Toast.LENGTH_SHORT).show()
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = context.getString(R.string.copy_text))
            }
        }
    }
}

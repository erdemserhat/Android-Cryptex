package com.erdemserhat.encryptext.presentation.screens.camera_scanner

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.erdemserhat.encryptext.R
import com.erdemserhat.encryptext.presentation.app.Screen
import com.erdemserhat.encryptext.presentation.app.SharedViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraScannerScreen(navController: NavController, viewModel: SharedViewModel) {
    // State to store the captured image URI
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // State to manage camera permission
    var isCameraPermissionGranted by remember { mutableStateOf(false) }

    // Current context and lifecycle owner
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // PreviewView to show the camera preview
    var previewView = remember { PreviewView(context) }

    // ImageCapture for taking pictures
    val imageCapture = remember { ImageCapture.Builder().build() }

    // Decoded text from the scanned image
    val decodedText = viewModel.decodedText

    // State to manage the visibility of the recognized text
    var shouldShowText by remember { mutableStateOf(false) }

    // State for password input and visibility control
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // States to toggle encryption and decryption modes
    var isEncryptionEnabled by remember { mutableStateOf(false) }
    var isDecryptionEnabled by remember { mutableStateOf(true) }

    // Effect to handle camera permission and start the camera when the composable is first launched
    LaunchedEffect(Unit) {
        // Check if camera permission is granted
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        // If permission is granted, start the camera
        if (isCameraPermissionGranted) {
            startCamera(previewView, imageCapture, context, lifecycleOwner)
        } else {
            // Request permission and start the camera
            requestCameraPermission(context)
            startCamera(previewView, imageCapture, context, lifecycleOwner)
        }
    }

    // Layout
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // If text has been decoded, navigate to the result screen
        if (shouldShowText) {
            viewModel.setMessage(decodedText)
            navController.navigate(Screen.ScannerResultScreen.route)
        } else {
            // Camera preview
            AndroidView(
                factory = { context ->
                    PreviewView(context).also { previewView = it }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

            // Capture image button
            Button(
                onClick = {
                    // Capture image and handle decryption or encryption based on mode
                    captureImage(
                        context = context,
                        imageCapture = imageCapture,
                        onDecoded = {
                            viewModel.setPasswordValue(password)
                            if (isDecryptionEnabled) viewModel.decrypt(it) else viewModel.encrypt(it)
                            navController.navigate(Screen.ScannerResultScreen.route)
                        }
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
            ) {
                Text(context.getString(R.string.operate))
            }
        }

        // Column for encryption/decryption selection and password input
        Column(modifier = Modifier.fillMaxSize().align(Alignment.TopCenter)) {
            Text(
                text = context.getString(R.string.operation_placeholder),
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )

            // Row for encryption mode toggle
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
                Text(text = context.getString(R.string.encryption), fontSize = 20.sp, color = Color.White)
            }

            // Row for decryption mode toggle
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
                Text(text = context.getString(R.string.decrypt), fontSize = 20.sp, color = Color.White)
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
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        val icon: Int = if (passwordVisible) R.drawable.visibility_eye_icon else R.drawable.visibility_off_eye_icon
                        Icon(painter = painterResource(id = icon), contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White
                )
            )
        }
    }
}

// Function to start the camera preview
private fun startCamera(
    previewView: PreviewView,
    imageCapture: ImageCapture,
    context: Context,
    lifecycleOwner: LifecycleOwner
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()
        val imageAnalysis = ImageAnalysis.Builder().build()
        val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA

        preview.setSurfaceProvider(previewView.surfaceProvider)
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
            processImageProxy(imageProxy)
        }

        cameraProvider.unbindAll() // Remove previously bound use cases
        try {
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis)
        } catch (exc: Exception) {
            Log.e("CameraX", "Binding failed", exc)
        }
    }, ContextCompat.getMainExecutor(context))
}

// Function to process the captured image proxy
private fun processImageProxy(imageProxy: ImageProxy) {
    imageProxy.close() // Close image after processing
}

// Request camera permission
private fun requestCameraPermission(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(Manifest.permission.CAMERA),
        1001
    )
}

// Function to capture an image and recognize text from it
private fun captureImage(
    context: Context,
    imageCapture: ImageCapture,
    onDecoded: (String) -> Unit
) {
    val photoFile = File(context.externalMediaDirs.first(), "photo.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                recognizeTextFromImage(photoFile, context, onDecoded) // Perform text recognition
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Error capturing image: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        })
}

// Function to perform text recognition using ML Kit
private fun recognizeTextFromImage(imageFile: File, context: Context, onDecoded: (String) -> Unit) {
    val image = InputImage.fromFilePath(context, Uri.fromFile(imageFile))
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())

    recognizer.process(image)
        .addOnSuccessListener { visionText -> processTextBlock(visionText, onDecoded) }
        .addOnFailureListener { e -> Log.e("MLKit", "Text recognition failed: ${e.message}") }
}

// Process the recognized text
private fun processTextBlock(result: com.google.mlkit.vision.text.Text, onDecoded: (String) -> Unit) {
    val resultText = result.text
    Log.d("MLKit", "Recognized text: $resultText")
    onDecoded(resultText)
}

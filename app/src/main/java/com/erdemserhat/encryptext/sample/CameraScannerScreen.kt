package com.erdemserhat.encryptext.sample

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScannerScreen(navController: NavController, viewModel: SharedViewModel) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val decodedText = viewModel.decodedText
    var shouldShowText by remember {
        mutableStateOf(false)
    }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isEncryptionEnabled by remember { mutableStateOf(false) }
    var isDecryptionEnabled by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (isCameraPermissionGranted) {
            startCamera(previewView, imageCapture, context, lifecycleOwner)
        } else {
            requestCameraPermission(context)
            startCamera(previewView, imageCapture, context, lifecycleOwner)

        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center


    ) {
        if (shouldShowText) {
            viewModel.setMessage(decodedText)
            navController.navigate(Screen.ScannerResultScreen.route)


        } else {
            AndroidView(
                factory = { context ->
                    PreviewView(context).also {
                        previewView = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            Button(
                onClick = {
                    captureImage(
                        context = context,
                        imageCapture = imageCapture,
                        onDecoded = {
                            viewModel.setPasswordValue(password)
                            if(isDecryptionEnabled) viewModel.decrypt(it)
                            else viewModel.encrypt(it)
                            navController.navigate(Screen.ScannerResultScreen.route)
                        }
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)

            )


            {
                Text(context.getString(R.string.operate))
            }

        }


        Column(modifier = Modifier.fillMaxSize().align(Alignment.TopCenter)) {
            Text(
                text = context.getString(R.string.operation_placeholder),
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp),
                color = Color.White
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

                Text(text = context.getString(R.string.encryption), fontSize = 20.sp,color = Color.White)

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

                Text(text = context.getString(R.string.decrypt), fontSize = 20.sp,color = Color.White)

            }

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
                        val icon: Int = if(passwordVisible) R.drawable.visibility_eye_icon else R.drawable.visibility_off_eye_icon
                        Icon(painter = painterResource(id = icon), contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedSupportingTextColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White

                )
            )
        }


    }


}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

        // Önceden bağlı olan tüm kullanım senaryolarını kaldırır
        cameraProvider.unbindAll()

        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
        } catch (exc: Exception) {
            Log.e("CameraX", "Binding failed", exc)
        }
    }, ContextCompat.getMainExecutor(context))
}
private fun processImageProxy(imageProxy: ImageProxy) {
    // Process image here
    imageProxy.close()
}

private fun requestCameraPermission(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(Manifest.permission.CAMERA),
        1001
    )
}

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
               // Toast.makeText(
                   // context,
                   // "Image saved: ${photoFile.absolutePath}",
                  //  Toast.LENGTH_SHORT
              //  ).show()
                recognizeTextFromImage(photoFile, context, onDecoded)
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(
                    context,
                    "Error capturing image: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
}

private fun recognizeTextFromImage(imageFile: File, context: Context, onDecoded: (String) -> Unit) {
    val image = InputImage.fromFilePath(context, Uri.fromFile(imageFile))

    val options = TextRecognizerOptions.Builder()
        .build()
    val recognizer = TextRecognition.getClient(options)

    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            processTextBlock(visionText, onDecoded)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
        }
}


private fun processTextBlock(
    visionText: com.google.mlkit.vision.text.Text,
    onDecoded: (String) -> Unit
) {
    val resultText = visionText.text
    Log.d("TextRecognition", "$resultText")
    onDecoded(resultText)
}

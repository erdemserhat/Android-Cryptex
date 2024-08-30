package com.asteriatech.androidcryptex.sample

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScannerScreen(navController: NavController,viewModel: SharedViewModel) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var decodedText by rememberSaveable {
        mutableStateOf("")
    }
    var shouldShowText by remember {
        mutableStateOf(false)
    }


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
                            decodedText = it
                            shouldShowText = true
                        }
                    )
                },
                modifier = Modifier.align(Alignment.BottomCenter).padding(20.dp)

                )


            {
                Text("Şifreyi Çöz")
            }

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
        val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA

        preview.setSurfaceProvider(previewView.surfaceProvider)

        //imageCapture = ImageCapture.Builder().build()

        val imageAnalysis = ImageAnalysis.Builder().build()
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
            processImageProxy(imageProxy)
        }

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
            imageAnalysis
        )
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
                Toast.makeText(
                    context,
                    "Image saved: ${photoFile.absolutePath}",
                    Toast.LENGTH_SHORT
                ).show()
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

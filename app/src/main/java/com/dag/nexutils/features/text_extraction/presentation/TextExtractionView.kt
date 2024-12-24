package com.dag.nexutils.features.text_extraction.presentation

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.dag.nexutils.base.components.CustomButton
import com.dag.nexutils.functions.extractText
import com.dag.nexutils.functions.uriToBitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException


@SuppressLint("ServiceCast")
@Composable
fun TextExtractionView() {
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                val image: InputImage
                try {
                    image = InputImage.fromFilePath(context, uri)
                    selectedImage = uriToBitmap(context, uri)
                    recognizer.process(image)
                        .addOnCompleteListener {
                            val text = extractText(it)
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Extracted Text", text)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT)
                                .show()
                        }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        selectedImage?.let {
            Image(
                it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        CustomButton(
            backgroundColor = Color.Red,
            modifier = Modifier.align(Alignment.BottomCenter),
            text = "Choose Photo"
        ) {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }
}
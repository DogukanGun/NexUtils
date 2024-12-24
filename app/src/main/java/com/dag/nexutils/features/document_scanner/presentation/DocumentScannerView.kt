package com.dag.nexutils.features.document_scanner.presentation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.dag.nexutils.base.components.CustomButton
import com.dag.nexutils.base.components.Visibility
import com.dag.nexutils.base.extensions.findActivity
import com.dag.nexutils.features.document_scanner.components.PdfViewerFromUri
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File


@Composable
fun DocumentScannerView() {
    var uri by remember { mutableStateOf(Uri.Builder().build()) }
    val options = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(false)
        .setPageLimit(2)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setScannerMode(SCANNER_MODE_FULL)
        .build()
    val context = LocalContext.current
    val activity = context.findActivity()
    val scanner = GmsDocumentScanning.getClient(options)
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                val gmsResult =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                gmsResult?.pages?.let { pages ->
                    for (page in pages) {
                        uri = pages[0].imageUri
                    }
                }
                gmsResult?.pdf?.let { pdf ->
                    uri = pdf.uri
                }
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(visible = uri.path.toString().isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                AnimatedVisibility(visible = uri.path.toString().isNotEmpty()) {
                    PdfViewerFromUri(uri)
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomButton(
                backgroundColor = Color.Red,
                text = "Scan"
            ) {
                scanner.getStartScanIntent(activity)
                    .addOnSuccessListener { intentSender ->
                        scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                    }
                    .addOnFailureListener {

                    }
            }
            AnimatedVisibility(visible = uri.path.toString().isNotEmpty()) {
                Spacer(modifier = Modifier.size(8.dp))
                CustomButton(
                    backgroundColor = Color.Blue,
                    text = "Share"
                ) {
                    val file = File(uri.path!!) // Convert your URI to a File object

                    val contentUri: Uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )

                    val shareIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, contentUri)
                        type = "application/pdf"
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission to the receiving app
                    }

                    val intent = Intent.createChooser(shareIntent, null)
                    startActivity(context, intent, null)
                }
            }
        }

    }

}

@Composable
@Preview
fun DocumentScannerPreview() {
    DocumentScannerView()
}
package com.dag.nexutils.features.document_scanner.components

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dag.nexutils.base.components.Visibility

@Composable
fun PdfViewerFromUri(uri: android.net.Uri) {
    val context = LocalContext.current

    var pdfPageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = java.io.File.createTempFile("tempPdf", ".pdf", context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream?.copyTo(output)
        }

        val fileDescriptor: ParcelFileDescriptor? =
            ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
        fileDescriptor?.let {
            val pdfRenderer = PdfRenderer(it)
            val page = pdfRenderer.openPage(0) // Open the first page
            val bitmap = android.graphics.Bitmap.createBitmap(
                page.width, page.height, android.graphics.Bitmap.Config.ARGB_8888
            )
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfPageBitmap = bitmap
            page.close()
            pdfRenderer.close()
        }
    }

    pdfPageBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}
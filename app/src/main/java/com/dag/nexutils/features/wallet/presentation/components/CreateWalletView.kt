package com.dag.nexutils.features.wallet.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dag.nexutils.base.components.LoadingIcon


@Composable
fun CreateWalletView(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please wait...",
                style = MaterialTheme.typography.headlineLarge
                    .copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Text(
                text = "your wallet is being generated",
                style = MaterialTheme.typography.labelMedium
                    .copy(color= Color.Gray),
                textAlign = TextAlign.Center
            )
        }
        LoadingIcon(
            modifier = Modifier
                .size(256.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun CreateWalletPreview(){
    Surface {
        CreateWalletView()
    }
}

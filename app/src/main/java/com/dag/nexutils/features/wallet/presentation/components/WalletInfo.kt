package com.dag.nexutils.features.wallet.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dag.nexutils.R

@Composable
fun WalletInfoView(
    balance: String,
    refresh: ()->Unit
){
    Card(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            contentColor = Color.Black,
            containerColor = Color.LightGray,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column {
                Text(
                    balance,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Total balance",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(
                onClick = refresh
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_reload),
                    contentDescription = "Reload"
                )
            }
        }

    }
}

@Composable
@Preview
fun WalletInfoPreview(){
    Surface(modifier = Modifier.fillMaxSize()) {
        WalletInfoView(balance = "20$"){}
    }
}
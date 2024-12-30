package com.dag.nexutils.features.wallet.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dag.nexutils.base.components.CustomButton
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WalletView(
    walletVM: WalletVM = hiltViewModel(),
    activityResultSender: ActivityResultSender? = null
) {
    val state = walletVM.viewState.collectAsState()
    var inputMint by remember { mutableStateOf("") }
    var outputMint by remember { mutableStateOf("") }
    var amount by remember { mutableIntStateOf(0) }
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        unfocusedTextColor = Color.LightGray,
        focusedTextColor = Color.Black
    )
    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
    ) {
        when (val innerState = state.value) {
            is WalletVS.Wallet -> {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .background(Color.White)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Trader powered by Jupiter",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = textFieldModifier,
                                textAlign = TextAlign.Center
                            )
                            OutlinedTextField(
                                inputMint,
                                {
                                    inputMint = it
                                },
                                colors = textFieldColors,
                                placeholder = {
                                    Text(
                                        "Input mint",
                                        style = MaterialTheme.typography.bodyMedium
                                            .copy(color = Color.LightGray)
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                modifier = textFieldModifier
                            )
                            OutlinedTextField(
                                outputMint,
                                {
                                    outputMint = it
                                },
                                colors = textFieldColors,
                                placeholder = {
                                    Text(
                                        "Output mint",
                                        style = MaterialTheme.typography.bodyMedium
                                            .copy(color = Color.LightGray)
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                modifier = textFieldModifier
                            )
                            OutlinedTextField(
                                amount.toString(),
                                {
                                    amount = it.toInt()
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                colors = textFieldColors,
                                placeholder = {
                                    Text(
                                        "Amount",
                                        style = MaterialTheme.typography.bodyMedium
                                            .copy(color = Color.LightGray)
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                modifier = textFieldModifier
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomButton(
                            backgroundColor = Color.Red,
                            text = "Make Trade",
                            textColor = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(8.dp)
                        ) {
                            if (activityResultSender != null) {
                                walletVM.connectWallet(activityResultSender)
                            }
                        }
                    }
                }
            }

            WalletVS.WalletCreate -> {
                CustomButton(
                    backgroundColor = Color.Red,
                    text = "Connect Your Wallet",
                    textColor = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    if (activityResultSender != null) {
                        walletVM.connectWallet(activityResultSender)
                    }
                }
            }

            else -> {}
        }
    }

}


@Composable
@Preview
fun WalletPreview() {
    WalletView()
}
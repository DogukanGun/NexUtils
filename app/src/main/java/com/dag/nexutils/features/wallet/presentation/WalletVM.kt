package com.dag.nexutils.features.wallet.presentation

import android.net.Uri
import android.provider.ContactsContract.Data
import androidx.lifecycle.viewModelScope
import com.dag.nexutils.base.BaseVM
import com.dag.nexutils.domain.DataPreferencesStore
import com.dag.nexutils.network.jupiter.usecase.TradeUseCase
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.mobilewalletadapter.clientlib.ConnectionIdentity
import com.solana.mobilewalletadapter.clientlib.MobileWalletAdapter
import com.solana.mobilewalletadapter.clientlib.successPayload
import com.solana.mobilewalletadapter.common.signin.SignInWithSolana
import com.solana.publickey.SolanaPublicKey
import com.solana.transaction.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletVM @Inject constructor(
    private val preferencesStore: DataPreferencesStore,
    val tradeUseCase: TradeUseCase
): BaseVM<WalletVS>() {

    lateinit var walletAdapter: MobileWalletAdapter

    init {
        checkWallet()
    }

    private fun checkWallet(){
        viewModelScope.launch {
            preferencesStore.removeAll()
            preferencesStore.read(DataPreferencesStore.PUBLIC_KEY).collectLatest{
                if(it?.isNotEmpty() == true){
                    _viewState.value = WalletVS.Wallet(
                        publicKey = SolanaPublicKey.from(it)
                    )
                }else{
                    _viewState.value = WalletVS.WalletCreate
                }
            }
        }
    }

    fun connectWallet(activityResultSender:ActivityResultSender){
        val solanaUri = Uri.parse("https://www.nexarb.com/")
        val iconUri = Uri.parse("favicon.ico") // resolves to https://yourdapp.com/favicon.ico
        val identityName = "NexUtils"

        walletAdapter = MobileWalletAdapter(connectionIdentity = ConnectionIdentity(
            identityUri = solanaUri,
            iconUri = iconUri,
            identityName = identityName
        )
        )

        viewModelScope.launch {
            val res = walletAdapter.signIn(activityResultSender,SignInWithSolana.Payload(
                "NexUtils",
                "NextUtils wants to connect to make a trade. After your signature, the app will create " +
                        "the transaction with your public key and then you will approve or reject the transaction"
            ))
            res.successPayload?.publicKey?.let {
                println("Wallet Address: $it")
                val publicKey = SolanaPublicKey(it)
                preferencesStore.write(DataPreferencesStore.PUBLIC_KEY,publicKey.string())
            }
        }
    }

    fun trade(
        inputMint: String,
        outputMint: String,
        inputAmount: Long,
        slippageBps: Int,
        walletAddress: SolanaPublicKey,
        activityResultSender:ActivityResultSender
    ){
        viewModelScope.launch {
            val response = tradeUseCase.execute(inputMint,outputMint,inputAmount,slippageBps,walletAddress)
            response?.let { transition->
                walletAdapter.transact(activityResultSender){
                    signAndSendTransactions(arrayOf(transition))
                }
            }
        }
    }
}
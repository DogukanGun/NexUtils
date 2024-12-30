package com.dag.nexutils.features.wallet.presentation

import com.dag.nexutils.base.BaseVS
import com.solana.publickey.PublicKey
import java.security.PrivateKey

sealed class WalletVS: BaseVS {
    data object WalletCreate: WalletVS()
    data class Wallet(
        val balance:Long? = 0,
        val publicKey: PublicKey? = null
    ): WalletVS()
}
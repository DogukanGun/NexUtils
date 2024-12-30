package com.dag.nexutils.network.jupiter.usecase

import com.dag.nexutils.network.jupiter.data.SwapRequest
import com.dag.nexutils.network.jupiter.service.TradeService
import com.solana.publickey.SolanaPublicKey
import javax.inject.Inject


class TradeUseCase @Inject constructor(
    val tradeService: TradeService
) {
    suspend fun execute(
        inputMint: String,
        outputMint: String,
        inputAmount: Long,
        slippageBps: Int,
        walletAddress: SolanaPublicKey
    ): ByteArray? {
        val quoteResponse = tradeService.getQuoteResponse(
            inputMint,
            outputMint,
            inputAmount * 1_000_000_000, // Convert to lamports
            slippageBps
        )

        if (quoteResponse.isSuccessful) {
            // Fetch swap transaction
            val swapTransactionResponse = tradeService.getSwapTransaction(
                SwapRequest(
                    quoteResponse.body()!!,
                    walletAddress.base58()
                )
            )

            if (swapTransactionResponse.isSuccessful) {
                val swapTransaction = swapTransactionResponse.body()?.swapTransaction.orEmpty()

                return android.util.Base64.decode(swapTransaction, android.util.Base64.DEFAULT)

            } else {
                println("Error in swap transaction: ${swapTransactionResponse.errorBody()?.string()}")
            }
        } else {
            println("Error in quote response: ${quoteResponse.errorBody()?.string()}")
        }
        return null
    }
}
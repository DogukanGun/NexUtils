package com.dag.nexutils.network.jupiter.data


data class QuoteResponse(val swapTransaction: String)
data class SwapRequest(
    val quoteResponse: QuoteResponse,
    val userPublicKey: String,
    val wrapAndUnwrapSol: Boolean = true,
    val dynamicComputeUnitLimit: Boolean = true,
    val prioritizationFeeLamports: String = "auto"
)

data class SwapTransactionResponse(val swapTransaction: String)

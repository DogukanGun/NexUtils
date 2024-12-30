package com.dag.nexutils.network.jupiter.service

import com.dag.nexutils.network.jupiter.data.QuoteResponse
import com.dag.nexutils.network.jupiter.data.SwapRequest
import com.dag.nexutils.network.jupiter.data.SwapTransactionResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.Response

interface TradeService {
    @GET("quote")
    suspend fun getQuoteResponse(
        @Query("inputMint") inputMint: String,
        @Query("outputMint") outputMint: String,
        @Query("amount") inputAmount: Long,
        @Query("slippageBps") slippageBps: Int,
        @Query("onlyDirectRoutes") onlyDirectRoutes: Boolean = true,
        @Query("maxAccounts") maxAccounts: Int = 20
    ): Response<QuoteResponse>

    @POST("v6/swap")
    suspend fun getSwapTransaction(
        @Body body: SwapRequest
    ): Response<SwapTransactionResponse>
}

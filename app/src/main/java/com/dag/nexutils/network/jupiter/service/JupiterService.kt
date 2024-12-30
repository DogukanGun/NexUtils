package com.dag.nexutils.network.jupiter.service

import com.dag.nexutils.network.jupiter.data.StakeRequest
import com.dag.nexutils.network.jupiter.data.StakeResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface JupiterService {
    @POST("https://worker.jup.ag/blinks/swap/So11111111111111111111111111111111111111112/jupSoLaHXQiZZTSfEWMTRRgpnyFm8f6sZdosWBjx93v/{amount}")
    suspend fun stakeWithJup(@Path("amount") amount: String,@Body body: StakeRequest):StakeResponse
}
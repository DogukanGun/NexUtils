package com.dag.nexutils.network.jupiter.data

data class StakeRequest(
    //account as base58
    val account:String
)

data class StakeResponse(
    //transaction as base58
    val transaction:String
)
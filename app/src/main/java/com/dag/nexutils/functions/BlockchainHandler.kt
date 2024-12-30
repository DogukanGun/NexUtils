package com.dag.nexutils.functions

import android.util.Log
import com.dag.nexutils.base.network.KtorHttpDriver
import com.solana.networking.Rpc20Driver
import com.solana.publickey.SolanaPublicKey
import com.solana.rpccore.JsonRpc20Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import java.util.UUID

@Serializable
data class BalanceResponse(val value: Long)
class InvalidAccountException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)

suspend fun getWalletBalance(address: SolanaPublicKey):Long{
    return withContext(Dispatchers.IO) {
        val rpc = Rpc20Driver("https://api.devnet.solana.com", KtorHttpDriver())
        val requestId = UUID.randomUUID().toString()
        val request = createBalanceRequest(address, requestId)
        val response = rpc.makeRequest(request, BalanceResponse.serializer())

        response.error?.let { error ->
            throw InvalidAccountException("Could not fetch balance for account [${address.base58()}]: ${error.code}, ${error.message}")
        }

        Log.d("SOL_BALANCE", "getBalance pubKey=${address.base58()}, balance=${response.result}")
        return@withContext response.result!!.value
    }
}

private fun createBalanceRequest(address: SolanaPublicKey, requestId: String = "1") =
    JsonRpc20Request(
        method = "getBalance",
        params = buildJsonArray {
            add(address.base58())
        },
        requestId
    )
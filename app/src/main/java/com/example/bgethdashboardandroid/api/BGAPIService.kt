package com.example.bgethdashboardandroid.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object BGAPIService {
    private const val BASE_URL = "https://bgethdashboardbackend-production.up.railway.app/api"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun fetchEthPrice(): Result<EthPriceResponse> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE_URL/eth-price")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP ${response.code}"))
                }

                val body = response.body?.string()
                    ?: return@withContext Result.failure(Exception("Empty response"))

                val ethPrice = json.decodeFromString<EthPriceResponse>(body)
                Result.success(ethPrice)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchGasPrice(): Result<GasPriceResponse> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE_URL/gas-price")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP ${response.code}"))
                }

                val body = response.body?.string()
                    ?: return@withContext Result.failure(Exception("Empty response"))

                val gasPrice = json.decodeFromString<GasPriceResponse>(body)
                Result.success(gasPrice)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

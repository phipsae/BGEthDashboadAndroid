package com.example.bgethdashboardandroid.api

import kotlinx.serialization.Serializable

@Serializable
data class EthPriceResponse(
    val priceUSD: Double,
    val timestamp: Long
)

@Serializable
data class GasPriceResponse(
    val gasPrice: String,
    val gasPriceGwei: Double,
    val baseFeePerGas: String,
    val baseFeePerGasGwei: Double,
    val timestamp: Long
)

package com.example.bgethdashboardandroid.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bgethdashboardandroid.api.BGAPIService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class WidgetUpdateWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting widget update...")
        return try {
            val data = fetchWidgetData()
            Log.d(TAG, "Fetched data: ETH=${data.ethPrice}, Gas=${data.gasPrice}")

            EthGasWidgetDataStore.saveData(context, data)
            Log.d(TAG, "Data saved to DataStore")

            // Update all widget instances
            EthGasWidget().updateAll(context)
            Log.d(TAG, "Widget updated successfully!")

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update widget", e)
            Result.retry()
        }
    }

    private suspend fun fetchWidgetData(): EthGasData = coroutineScope {
        Log.d(TAG, "Fetching data from API...")

        val ethPriceDeferred = async { BGAPIService.fetchEthPrice() }
        val gasPriceDeferred = async { BGAPIService.fetchGasPrice() }

        val ethResult = ethPriceDeferred.await()
        val gasResult = gasPriceDeferred.await()

        Log.d(TAG, "ETH Result: ${ethResult.getOrNull()}, Error: ${ethResult.exceptionOrNull()?.message}")
        Log.d(TAG, "Gas Result: ${gasResult.getOrNull()}, Error: ${gasResult.exceptionOrNull()?.message}")

        val ethPrice = ethResult.getOrNull()
        val gasPrice = gasResult.getOrNull()

        EthGasData(
            ethPrice = ethPrice?.let { EthGasData.formatEthPrice(it.priceUSD) } ?: "—",
            gasPrice = gasPrice?.let { EthGasData.formatGasPrice(it.baseFeePerGasGwei) } ?: "—",
            gasPriceValue = gasPrice?.baseFeePerGasGwei ?: 0.0,
            lastUpdated = EthGasData.formatTime()
        )
    }

    companion object {
        private const val TAG = "WidgetUpdateWorker"
    }
}

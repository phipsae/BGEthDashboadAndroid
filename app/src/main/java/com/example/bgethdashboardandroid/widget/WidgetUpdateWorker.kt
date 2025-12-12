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
        return try {
            val data = fetchWidgetData()
            EthGasWidgetDataStore.saveData(context, data)

            // Update all widget instances
            EthGasWidget().updateAll(context)

            Log.d(TAG, "Widget updated: ETH=${data.ethPrice}, Gas=${data.gasPrice}")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update widget", e)
            Result.retry()
        }
    }

    private suspend fun fetchWidgetData(): EthGasData = coroutineScope {
        val ethPriceDeferred = async { BGAPIService.fetchEthPrice() }
        val gasPriceDeferred = async { BGAPIService.fetchGasPrice() }

        val ethResult = ethPriceDeferred.await()
        val gasResult = gasPriceDeferred.await()

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

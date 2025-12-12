package com.example.bgethdashboardandroid.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.widgetDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "eth_gas_widget_data"
)

object EthGasWidgetDataStore {
    private val ETH_PRICE_KEY = stringPreferencesKey("eth_price")
    private val GAS_PRICE_KEY = stringPreferencesKey("gas_price")
    private val GAS_PRICE_VALUE_KEY = doublePreferencesKey("gas_price_value")
    private val LAST_UPDATED_KEY = stringPreferencesKey("last_updated")

    suspend fun saveData(context: Context, data: EthGasData) {
        context.widgetDataStore.edit { preferences ->
            preferences[ETH_PRICE_KEY] = data.ethPrice
            preferences[GAS_PRICE_KEY] = data.gasPrice
            preferences[GAS_PRICE_VALUE_KEY] = data.gasPriceValue
            preferences[LAST_UPDATED_KEY] = data.lastUpdated
        }
    }

    suspend fun getData(context: Context): EthGasData {
        return context.widgetDataStore.data.map { preferences ->
            EthGasData(
                ethPrice = preferences[ETH_PRICE_KEY] ?: "—",
                gasPrice = preferences[GAS_PRICE_KEY] ?: "—",
                gasPriceValue = preferences[GAS_PRICE_VALUE_KEY] ?: 0.0,
                lastUpdated = preferences[LAST_UPDATED_KEY] ?: "--:--"
            )
        }.first()
    }
}

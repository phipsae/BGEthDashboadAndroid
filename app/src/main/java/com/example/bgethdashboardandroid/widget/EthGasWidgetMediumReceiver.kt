package com.example.bgethdashboardandroid.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class EthGasWidgetMediumReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = EthGasWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        EthGasWidgetReceiver.triggerImmediateUpdate(context)
        EthGasWidgetReceiver.scheduleWidgetUpdates(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: android.appwidget.AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        EthGasWidgetReceiver.triggerImmediateUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Don't cancel work here - other widget sizes might still be active
    }
}

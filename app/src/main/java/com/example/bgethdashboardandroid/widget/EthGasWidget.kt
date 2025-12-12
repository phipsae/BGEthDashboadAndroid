package com.example.bgethdashboardandroid.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.ColorFilter
import androidx.glance.Image
import androidx.glance.ImageProvider
import com.example.bgethdashboardandroid.MainActivity
import com.example.bgethdashboardandroid.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EthGasWidget : GlanceAppWidget() {

    companion object {
        private val SMALL_SIZE = DpSize(110.dp, 110.dp)
        private val MEDIUM_SIZE = DpSize(250.dp, 110.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(SMALL_SIZE, MEDIUM_SIZE)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = EthGasWidgetDataStore.getData(context)

        provideContent {
            GlanceTheme {
                WidgetContent(data = data)
            }
        }
    }

    @Composable
    private fun WidgetContent(data: EthGasData) {
        val size = LocalSize.current
        val isWide = size.width >= 200.dp

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.widget_background))
                .clickable(actionStartActivity<MainActivity>())
        ) {
            if (isWide) {
                MediumWidgetLayout(data = data)
            } else {
                SmallWidgetLayout(data = data)
            }
        }
    }

    @Composable
    private fun SmallWidgetLayout(data: EthGasData) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // BG Logo in top right
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_bg_logo),
                    contentDescription = "BG Logo",
                    modifier = GlanceModifier.size(18.dp),
                    colorFilter = ColorFilter.tint(ColorProvider(WidgetColors.LogoTint))
                )
            }

            // Main content
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = GlanceModifier.height(8.dp))

                // Ethereum label
                Text(
                    text = "Ethereum",
                    style = TextStyle(
                        color = ColorProvider(WidgetColors.LabelColor),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                // ETH Price
                Text(
                    text = data.ethPrice,
                    style = TextStyle(
                        color = ColorProvider(WidgetColors.PrimaryText),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = GlanceModifier.height(4.dp))

                // Gas row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_gas_pump),
                        contentDescription = "Gas",
                        modifier = GlanceModifier.size(12.dp),
                        colorFilter = ColorFilter.tint(ColorProvider(WidgetColors.CyanAccent))
                    )
                    Spacer(modifier = GlanceModifier.width(4.dp))
                    Text(
                        text = "${data.gasPrice} gwei",
                        style = TextStyle(
                            color = ColorProvider(WidgetColors.SecondaryText),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.defaultWeight())

                // Timestamp
                Text(
                    text = data.lastUpdated,
                    style = TextStyle(
                        color = ColorProvider(WidgetColors.TertiaryText),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            // Refresh button in bottom right
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_refresh),
                    contentDescription = "Refresh",
                    modifier = GlanceModifier
                        .size(16.dp)
                        .clickable(actionRunCallback<RefreshWidgetAction>()),
                    colorFilter = ColorFilter.tint(ColorProvider(WidgetColors.TertiaryText))
                )
            }
        }
    }

    @Composable
    private fun MediumWidgetLayout(data: EthGasData) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // BG Logo in top right
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_bg_logo),
                    contentDescription = "BG Logo",
                    modifier = GlanceModifier.size(18.dp),
                    colorFilter = ColorFilter.tint(ColorProvider(WidgetColors.LogoTint))
                )
            }

            // Main content - two columns
            Row(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ETH Price section
                Column(
                    modifier = GlanceModifier.defaultWeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ethereum",
                        style = TextStyle(
                            color = ColorProvider(WidgetColors.LabelColor),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = GlanceModifier.height(2.dp))

                    Text(
                        text = data.ethPrice,
                        style = TextStyle(
                            color = ColorProvider(WidgetColors.PrimaryText),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = GlanceModifier.height(2.dp))

                    Text(
                        text = data.lastUpdated,
                        style = TextStyle(
                            color = ColorProvider(WidgetColors.TertiaryText),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Divider
                Box(
                    modifier = GlanceModifier
                        .width(1.dp)
                        .height(60.dp)
                        .background(WidgetColors.DividerColor)
                ) {}

                // Gas section
                Column(
                    modifier = GlanceModifier.defaultWeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gas label with icon
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            provider = ImageProvider(R.drawable.ic_gas_pump),
                            contentDescription = "Gas",
                            modifier = GlanceModifier.size(11.dp),
                            colorFilter = ColorFilter.tint(ColorProvider(WidgetColors.CyanAccent))
                        )
                        Spacer(modifier = GlanceModifier.width(4.dp))
                        Text(
                            text = "Gas",
                            style = TextStyle(
                                color = ColorProvider(WidgetColors.LabelColor),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    Spacer(modifier = GlanceModifier.height(4.dp))

                    Text(
                        text = data.gasPrice,
                        style = TextStyle(
                            color = ColorProvider(WidgetColors.PrimaryText),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "gwei",
                        style = TextStyle(
                            color = ColorProvider(WidgetColors.LabelColor),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = GlanceModifier.height(6.dp))

                    // Gas level indicator (5 bars)
                    GasLevelIndicator(level = data.gasLevel)
                }
            }

            // Refresh button in bottom right
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_refresh),
                    contentDescription = "Refresh",
                    modifier = GlanceModifier
                        .size(16.dp)
                        .clickable(actionRunCallback<RefreshWidgetAction>()),
                    colorFilter = ColorFilter.tint(ColorProvider(WidgetColors.TertiaryText))
                )
            }
        }
    }

    @Composable
    private fun GasLevelIndicator(level: Int) {
        Row(horizontalAlignment = Alignment.CenterHorizontally) {
            for (i in 1..5) {
                val isActive = i <= level
                Box(
                    modifier = GlanceModifier
                        .width(8.dp)
                        .height(4.dp)
                        .background(
                            if (isActive) WidgetColors.CyanAccent else WidgetColors.InactiveBar
                        )
                ) {}
                if (i < 5) {
                    Spacer(modifier = GlanceModifier.width(3.dp))
                }
            }
        }
    }
}

// Data class for widget state
data class EthGasData(
    val ethPrice: String = "—",
    val gasPrice: String = "—",
    val gasPriceValue: Double = 0.0,
    val lastUpdated: String = "--:--"
) {
    // Calculate gas level (1-5) based on gwei price
    // <0.5 = Ultra-low, 0.5-3 = Typical, 3-15 = Busy, 15-60 = High, >60 = Spike
    val gasLevel: Int
        get() = when {
            gasPriceValue < 0.5 -> 1
            gasPriceValue < 3 -> 2
            gasPriceValue < 15 -> 3
            gasPriceValue < 60 -> 4
            else -> 5
        }

    companion object {
        fun formatEthPrice(priceUSD: Double): String {
            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
            formatter.maximumFractionDigits = 0
            return formatter.format(priceUSD)
        }

        fun formatGasPrice(gasPriceGwei: Double): String {
            return when {
                gasPriceGwei < 1 -> String.format(Locale.US, "%.3f", gasPriceGwei)
                gasPriceGwei < 10 -> String.format(Locale.US, "%.2f", gasPriceGwei)
                else -> String.format(Locale.US, "%.0f", gasPriceGwei)
            }
        }

        fun formatTime(timestamp: Long = System.currentTimeMillis()): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}

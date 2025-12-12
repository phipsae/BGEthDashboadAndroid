package com.example.bgethdashboardandroid.ui.theme

import androidx.compose.ui.graphics.Color

// Default Material colors (kept for compatibility)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// App theme colors matching iOS design
object BGColors {
    // Background gradient colors
    val BackgroundStart = Color(0xFF0D0D1F)
    val BackgroundMid = Color(0xFF14142E)
    val BackgroundEnd = Color(0xFF1E1A38)

    // Text colors
    val PrimaryText = Color.White
    val SecondaryText = Color.White.copy(alpha = 0.85f)
    val TertiaryText = Color.White.copy(alpha = 0.6f)
    val SubtleText = Color.White.copy(alpha = 0.4f)

    // Accent colors
    val CyanAccent = Color(0xFF00BCD4)
    val PurpleAccent = Color(0xFF9C27B0)

    // UI elements
    val CardBackground = Color.White.copy(alpha = 0.08f)
    val CardBorder = Color.White.copy(alpha = 0.15f)
    val DividerColor = Color.White.copy(alpha = 0.1f)
}

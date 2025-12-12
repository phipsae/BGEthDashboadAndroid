package com.example.bgethdashboardandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bgethdashboardandroid.widget.EthGasWidgetReceiver
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bgethdashboardandroid.ui.theme.BGETHDashboardAndroidTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Trigger widget update when app opens (helps ensure data is fresh)
        Log.d("MainActivity", "Triggering widget update...")
        EthGasWidgetReceiver.triggerImmediateUpdate(this)

        setContent {
            BGETHDashboardAndroidTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BGEthTrackerScreen()
                }
            }
        }
    }
}

// App Colors matching iOS design
object AppColors {
    val BackgroundStart = Color(0xFF0D0D1F)
    val BackgroundMid = Color(0xFF14142E)
    val BackgroundEnd = Color(0xFF1E1A38)

    val PrimaryText = Color.White
    val SecondaryText = Color.White.copy(alpha = 0.85f)
    val TertiaryText = Color.White.copy(alpha = 0.6f)
    val SubtleText = Color.White.copy(alpha = 0.4f)

    val CyanAccent = Color(0xFF00BCD4)
    val PurpleAccent = Color(0xFF9C27B0)

    val CardBackground = Color.White.copy(alpha = 0.08f)
    val CardBorder = Color.White.copy(alpha = 0.15f)
    val DividerColor = Color.White.copy(alpha = 0.1f)

    val GradientStepStart = Color(0xFF00BCD4).copy(alpha = 0.3f)
    val GradientStepEnd = Color(0xFF9C27B0).copy(alpha = 0.3f)
}

@Composable
fun BGEthTrackerScreen() {
    // Animation states
    var animationStarted by remember { mutableStateOf(false) }

    val logoScale by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0.8f,
        animationSpec = tween(durationMillis = 800),
        label = "logoScale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "logoAlpha"
    )

    var titleVisible by remember { mutableStateOf(false) }
    val titleAlpha by animateFloatAsState(
        targetValue = if (titleVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "titleAlpha"
    )

    var instructionsVisible by remember { mutableStateOf(false) }
    val instructionsAlpha by animateFloatAsState(
        targetValue = if (instructionsVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "instructionsAlpha"
    )

    // Trigger animations sequentially
    LaunchedEffect(Unit) {
        animationStarted = true
        delay(300)
        titleVisible = true
        delay(200)
        instructionsVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        AppColors.BackgroundStart,
                        AppColors.BackgroundMid,
                        AppColors.BackgroundEnd
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        // Subtle dot pattern overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dotColor = Color.White.copy(alpha = 0.03f)
            val spacing = 40.dp.toPx()

            var x = 0f
            while (x < size.width) {
                var y = 0f
                while (y < size.height) {
                    drawCircle(
                        color = dotColor,
                        radius = 1f,
                        center = Offset(x, y)
                    )
                    y += spacing
                }
                x += spacing
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            // Logo with animation and shadow
            Box(
                modifier = Modifier
                    .scale(logoScale)
                    .alpha(logoAlpha),
                contentAlignment = Alignment.Center
            ) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    AppColors.PurpleAccent.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_bg_logo),
                    contentDescription = "BG Logo",
                    modifier = Modifier.size(160.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title with gradient
            Text(
                text = "BG Eth Tracker",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.PrimaryText,
                modifier = Modifier.alpha(titleAlpha)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Instructions Card
            InstructionsCard(
                modifier = Modifier.alpha(instructionsAlpha)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Text(
                text = "Track ETH price & gas fees at a glance",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = AppColors.SubtleText,
                modifier = Modifier.alpha(instructionsAlpha)
            )
        }
    }
}

@Composable
fun InstructionsCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AppColors.CardBackground,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.2f),
                    Color.White.copy(alpha = 0.05f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_widget),
                    contentDescription = null,
                    tint = AppColors.CyanAccent,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Add the Widget",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.PrimaryText
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = AppColors.DividerColor,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Instructions
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InstructionRow(
                    step = "1",
                    iconRes = R.drawable.ic_touch,
                    text = "Long press on your Home Screen"
                )

                InstructionRow(
                    step = "2",
                    iconRes = R.drawable.ic_widgets,
                    text = "Tap \"Widgets\" from the menu"
                )

                InstructionRow(
                    step = "3",
                    iconRes = R.drawable.ic_search,
                    text = "Search for \"ETH & Gas\""
                )

                InstructionRow(
                    step = "4",
                    iconRes = R.drawable.ic_check_circle,
                    text = "Drag it to your Home Screen"
                )
            }
        }
    }
}

@Composable
fun InstructionRow(
    step: String,
    iconRes: Int,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Step number with gradient background
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            AppColors.GradientStepStart,
                            AppColors.GradientStepEnd
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.PrimaryText
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Icon
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = AppColors.CyanAccent.copy(alpha = 0.8f),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Text
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.SecondaryText,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BGEthTrackerScreenPreview() {
    BGETHDashboardAndroidTheme(darkTheme = true, dynamicColor = false) {
        BGEthTrackerScreen()
    }
}

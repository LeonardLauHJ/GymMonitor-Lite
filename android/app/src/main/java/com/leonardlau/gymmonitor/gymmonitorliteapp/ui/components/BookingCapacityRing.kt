package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A UI composable displaying a circular ring showing booking capacity for a gym class.
 * Displays the proportion of current bookings to max capacity visually on the ring,
 * with the numbers displayed in the center.
 *
 * @param current Number of current bookings.
 * @param max Maximum capacity of the class.
 * @param size Diameter of the ring. Default is 50.dp.
 * @param strokeWidth Thickness of the ring. Default is 6.dp.
 * @param backgroundColor Color for the unfilled portion of the ring. Default is LightGray.
 * @param foregroundColor Color for the filled portion of the ring. Default is a bluish purple.
 * @param fontSize Font size of the text inside the ring. Default is 14.sp.
 */
@Composable
fun BookingCapacityRing(
    current: Int,
    max: Int,
    size: Dp = 50.dp,
    strokeWidth: Dp = 4.dp,
    backgroundColor: Color = Color.LightGray,
    foregroundColor: Color = Color(0xFF495D91),
    fontSize: TextUnit = 14.sp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        // Canvas is used for drawing shapes manually
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw the background ring
            drawArc(
                color = backgroundColor,
                startAngle = 0f, // start at the right-hand side of the circle
                sweepAngle = 360f, // draw a full circle
                useCenter = false, // false = only draw the outline, not a filled pie slice
                style = Stroke(strokeWidth.toPx()) // thickness of the ring
            )

            // Compute how much of the circle should be filled
            val sweep = 360f * (current.toFloat() / max) // proportion of booking to max capacity

            // Draw the filled portion on top of the background
            drawArc(
                color = foregroundColor,
                startAngle = -90f, // start from top
                sweepAngle = sweep, // how much of the circle to fill
                useCenter = false,
                style = Stroke(strokeWidth.toPx())
            )
        }

        // Add the text in the center
        Text(
            text = "$current/$max", // Displays as a fraction x/y
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
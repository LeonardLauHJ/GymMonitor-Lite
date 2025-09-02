package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * A reusable component for page title text.
 *
 * @param text The text to display.
 * @param color The text colour, defaults to black.
 * @param fontSize Text font size, default 25.sp.
 * @param fontWeight Text font weight, default Bold.
 * @param modifier Optional Modifier for layout/styling.
 */
@Composable
fun ScreenTitle(
    text: String,
    color: Color = Color.Black,
    fontSize: androidx.compose.ui.unit.TextUnit = 25.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            modifier = Modifier
        )
    }
}

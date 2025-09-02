package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.PrimaryBlue

/**
 * A reusable component for page headers.
 *
 * @param text The text to display.
 * @param textColor The text colour, defaults to white.
 * @param backgroundColor The background colour, defaults to the primary blue.
 * @param fontSize Text font size, default 25.sp.
 * @param fontWeight Text font weight, default Bold.
 * @param modifier Optional Modifier for layout/styling.
 */
@Composable
fun PageHeader(
    text: String,
    textColor: Color = Color.White,
    backgroundColor: Color = PrimaryBlue,
    fontSize: androidx.compose.ui.unit.TextUnit = 25.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        // Page title text
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            modifier = Modifier.offset(y = 10.dp), // Shift downwards
        )
    }

}
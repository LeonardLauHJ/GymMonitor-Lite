package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

/**
 * A reusable UI composable for the dashboard stats,
 * consisting of a value on top and a label underneath it.
 *
 * @param value The main value to display above the label.
 * @param label The text label or description to display under the value
 * @param color The text colour, defaults to black
 * @param valueFontSize The font size of the main value. Defaults to the headlineSmall typography size.
 * @param labelFontSize The font size of the label. Defaults to the bodyMedium typography size.
 * @param modifier Optional Modifier for layout/styling.
 */
@Composable
fun DashboardStat(
    value: String,
    label: String,
    color: Color = Color(0xFF000000),
    valueFontSize: TextUnit = MaterialTheme.typography.headlineSmall.fontSize,
    labelFontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    modifier: Modifier = Modifier
) {
    Column(
        // Stack the Value on top of the middle of the Label
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Value
        Text(
            text = value,
            fontSize = valueFontSize,
            fontWeight = FontWeight.Bold
        )

        // Label
        Text(
            text = label,
            fontSize = labelFontSize
        )
    }
}

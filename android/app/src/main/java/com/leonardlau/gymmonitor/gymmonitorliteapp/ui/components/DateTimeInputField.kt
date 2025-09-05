package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Input field for displaying a date/time value that the user cannot type in directly.
 * Tapping the field should trigger a date/time picker or similar selection UI.
 * Styled to look identical to an OutlinedTextField.
 *
 * @param value The current text value to display (e.g. "18 Aug 2050, 7:30 AM").
 * @param label The label to show above the field.
 * @param onClick Callback triggered when the field is tapped.
 * @param modifier Optional [Modifier] for styling and layout.
 */
@Composable
fun DateTimeInputField(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Put the text inside of a clickable box. Clicking the box will activate the onClick().
    // NOTE: I first tried to use a disabled OutlinedTextField with a clickable modifier, then
    // trying to use a clickable Box with the OutlinedTextField inside. Both methods did not
    // activate the onClick(), as the OutlinedTextField appeared to eat the click.
    Box(
        // Styled like an OutlinedTextField
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            // If the value is blank, show the label. Otherwise show the value.
            text = value.ifBlank { label },
            color = if (value.isNotBlank()) Color.Black else Color.DarkGray
        )
    }
}

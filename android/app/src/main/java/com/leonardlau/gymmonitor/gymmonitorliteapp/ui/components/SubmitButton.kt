package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A reusable button for submitting forms that may trigger a network request.
 * Displays a loading spinner when [isLoading] is true.
 *
 * @param text The text to display on the button.
 * @param isLoading If true, shows a loading spinner and disables the button from further presses.
 *                  If false, shows the text.
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier Optional Modifier for styling, sizing, or layout.
 */
@Composable
fun SubmitButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = !isLoading, // Button is only usable if it is not already in progress
        modifier = modifier
    ) {
        // If isLoading is true, display a loading spinner icon
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        } else {
            // Otherwise show the text as normal
            Text(text)
        }
    }
}

package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

/**
 * Input field for numeric input only.
 *
 * @param value The current text value of the input field.
 * @param onValueChange Callback triggered when the value changes.
 * @param label String to use as the input field label
 * @param modifier Optional Modifier for styling and layout.
 */
@Composable
fun NumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Only update the value if the full value is made up of number digits
            // i.e. do not allow any non-digit characters
            if (newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            // For on-screen keyboard, use the numeric keypad
            keyboardType = KeyboardType.Number
        ),
        modifier = modifier
    )
}

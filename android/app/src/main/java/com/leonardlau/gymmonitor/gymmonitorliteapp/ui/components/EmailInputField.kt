package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

/**
 * Input field for emails.
 *
 * @param value The current text value of the input field.
 * @param onValueChange Callback triggered when the text changes.
 * @param modifier Optional Modifier for styling and layout.
 */
@Composable
fun EmailInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            // For on-screen keyboard, use the email keyboard type (has easy access to @ symbol)
            keyboardType = KeyboardType.Email
        ),
        modifier = modifier
    )
}

package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Input field for regular text.
 *
 * @param value The current text value of the input field.
 * @param onValueChange Callback triggered when the text changes.
 * @param label String to use as the input field label
 * @param modifier Optional Modifier for styling and layout.
 */
@Composable
fun TextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier
    )
}

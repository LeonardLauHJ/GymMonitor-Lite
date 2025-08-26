package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Input field for passwords, with a toggle to show or hide the password text.
 *
 * @param value The current text value of the input field.
 * @param onValueChange Callback triggered when the text changes.
 * @param modifier Optional Modifier for styling and layout.
 */
@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // State variable to track if password is visible
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = modifier,

        // Show password text if passwordVisible is true, otherwise mask it
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        // Eye icon button at the end for toggling visibility
        trailingIcon = {
            // The image icon to use will depend on the value of passwordVisible
            val image = if (passwordVisible) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff

            // When the icon is clicked, flip the value of the passwordVisible state variable
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                // Update the icon's image and description accordingly
                Icon(
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        }
    )
}

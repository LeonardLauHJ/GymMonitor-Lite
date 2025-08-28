package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.EmailInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.LinkText
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.PasswordInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.ScreenTitle
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.SubmitButton

/**
 * UI composable for the signup screen.
 * Only contains UI. State and logic need to be provided from a ViewModel.
 *
 * @param email Current email input value
 * @param password Current password input value
 * @param isLoading Whether a signup request is currently in progress
 * @param onEmailChange Callback when email input changes
 * @param onPasswordChange Callback when password input changes
 * @param onLoginClick Callback when the login button is clicked
 * @param onNavigateToSignUp Callback when user taps the "Sign Up" link
 */
@Composable
fun LoginPage(
    email: String,
    password: String,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(2f))

        // Screen Title
        ScreenTitle(
            text = "Log In"
        )

        // Input fields

        EmailInputField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth()
        )

        PasswordInputField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth()
        )

        // Submit button
        SubmitButton(
            text = "Sign Up",
            isLoading = isLoading,
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        )

        // Link to Sign Up page
        LinkText(
            text = "Don't have an account? Sign up",
            onClick = onNavigateToSignUp
        )

        Spacer(modifier = Modifier.weight(3f))
    }
}
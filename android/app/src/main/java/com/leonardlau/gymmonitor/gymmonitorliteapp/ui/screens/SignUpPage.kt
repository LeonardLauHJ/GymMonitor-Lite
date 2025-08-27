package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.EmailInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.LinkText
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.NumberInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.PasswordInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.SubmitButton
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.TextInputField

/**
 * UI composable for the signup screen.
 * Only contains UI. State and logic need to be provided from a ViewModel.
 *
 * @param name Current name input value
 * @param email Current email input value
 * @param password Current password input value
 * @param clubCode Current club code input value
 * @param membershipPlanId Current membership plan ID input value
 * @param isLoading Whether a signup request is currently in progress
 * @param onNameChange Callback when name input changes
 * @param onEmailChange Callback when email input changes
 * @param onPasswordChange Callback when password input changes
 * @param onClubCodeChange Callback when club code input changes
 * @param onMembershipPlanChange Callback when membership plan ID input changes
 * @param onSignUpClick Callback when the signup button is clicked
 * @param onNavigateToLogin Callback when user taps the "Log in" link
 */
@Composable
fun SignUpPage(
    name: String,
    email: String,
    password: String,
    clubCode: String,
    membershipPlanId: String,
    isLoading: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onClubCodeChange: (String) -> Unit,
    onMembershipPlanChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Screen Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign Up",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Input fields
        TextInputField(
            value = name,
            onValueChange = onNameChange,
            label = "Name",
            modifier = Modifier.fillMaxWidth()
        )

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

        TextInputField(
            value = clubCode,
            onValueChange = onClubCodeChange,
            label = "Club Code",
            modifier = Modifier.fillMaxWidth()
        )

        NumberInputField(
            value = membershipPlanId,
            onValueChange = onMembershipPlanChange,
            label = "Membership Plan ID",
            modifier = Modifier.fillMaxWidth()
        )

        // Submit button
        SubmitButton(
            text = "Sign Up",
            isLoading = isLoading,
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        )

        // Link to Log In page
        LinkText(
            text = "Already have an account? Log in",
            onClick = onNavigateToLogin
        )

        Spacer(modifier = Modifier.weight(3f))
    }
}

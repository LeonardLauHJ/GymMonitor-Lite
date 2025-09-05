package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * LandingPage
 * Pure UI composable for the landing screen. Displays the app logo and
 * buttons for signing up or logging in.
 *
 * @param onSignUpClick Lambda invoked when the "Sign Up" button is clicked.
 * @param onLoginClick Lambda invoked when the "Log In" button is clicked.
 */
@Composable
fun LandingPage(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(2f))

        // GymMonitor Lite logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "GymMonitor Lite",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Sign Up button
        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        // Log In button
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.weight(3f))
    }
}

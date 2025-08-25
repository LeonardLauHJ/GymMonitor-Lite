package com.leonardlau.gymmonitor.gymmonitorliteapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Landing Page
 * The first screen shown to users when they open the app.
 *
 * @param navController NavController used to navigate between screens.
 */
@Composable
fun LandingPage(navController: NavController) {
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

        // Sign Up page button
        Button(
            onClick = {
                navController.navigate("signup")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        // Log In page Button
        Button(
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.weight(3f))
    }

}
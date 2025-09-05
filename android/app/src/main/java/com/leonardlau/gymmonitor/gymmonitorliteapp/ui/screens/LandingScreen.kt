package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences

/**
 * LandingScreen
 * The first screen shown to users when they open the app.
 *
 * @param navController NavController used to navigate between screens.
 * @param userPrefs UserPreferences used to read stored JWT token.
 */
@Composable
fun LandingScreen(
    navController: NavController,
    userPrefs: UserPreferences
) {
    // Render pure UI
    LandingPage(
        onSignUpClick = { navController.navigate("signup") },
        onLoginClick = { navController.navigate("login") }
    )
}

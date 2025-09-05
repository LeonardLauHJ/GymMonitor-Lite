package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.first

/**
 * LandingScreen
 * The first screen shown to users when they open the app.
 * Passes the navigation functions to the [LandingPage] UI and also handles auto-logging in
 * users if they already have a valid JWT token stored.
 *
 * @param navController NavController used to navigate between screens.
 * @param userPrefs UserPreferences used to read stored JWT token.
 */
@Composable
fun LandingScreen(
    navController: NavController,
    userPrefs: UserPreferences
) {
    // Create an instance of the auth repository to be able to check the user's auth token
    // This would typically be done in a separate viewmodel, but a new viewmodel just for
    // this is slightly overkill.
    val authRepository = remember { AuthRepository() }

    // When this screen is first displayed
    // Automatically log in the user if they have a valid JWT token stored
    LaunchedEffect(Unit) {
        // Fetch the current JWT auth token value from the datastore
        val token = userPrefs.token.first()
        if (token.isNotBlank()) {
            // Call the checkAuth backend endpoint with the token to see if the user is already
            // logged in. If so, they will have a non-null role.
            val role = authRepository.checkAuth(token).getOrNull()?.role

            // If the user has a valid JWT token, navigate them based on their role
            if (role == "MEMBER") {
                navController.navigate("dashboard") { popUpTo("landing") { inclusive = true } }
            }
            if (role == "STAFF") {
                navController.navigate("clubMembersOverview") { popUpTo("landing") { inclusive = true } }
            }
        }
    }

    // Render pure UI
    LandingPage(
        onSignUpClick = { navController.navigate("signup") },
        onLoginClick = { navController.navigate("login") }
    )
}

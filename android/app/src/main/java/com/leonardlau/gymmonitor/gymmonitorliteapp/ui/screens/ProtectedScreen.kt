package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * General-purpose protected screen wrapper.
 *
 * Ensures that only users with the specified role can access the wrapped content.
 * If the user is not logged in or does not have the required role, they are redirected
 * to the landing page and shown a Toast message.
 *
 * @param requiredRole The role required to access this screen (e.g., "MEMBER").
 * @param navController NavController used to redirect unauthorized users.
 * @param userPrefs UserPreferences to get stored JWT token.
 * @param authRepository AuthRepository for checking user authentication.
 * @param content Composable to display if user is authorized.
 */
@Composable
fun ProtectedScreen(
    requiredRole: String,
    navController: NavController,
    userPrefs: UserPreferences,
    authRepository: AuthRepository,
    content: @Composable () -> Unit // Composable lambda to render the screen content
) {
    LaunchedEffect(Unit) {
        // Get the current authentication token
        val token = userPrefs.token.first()

        // If no token exists, the user is not logged in, no need to ask checkAuth.
        if (token.isEmpty()) {
            // Send them back to the landing page with a Toast message telling them to log in.
            Toast.makeText(navController.context, "Login required",
                            Toast.LENGTH_LONG).show()
            // Redirect to the landing page
            navController.navigate("landing") {
                // Remove history up to and including the landing page we are redirecting to
                // so that the user cannot press back and return here
                popUpTo("landing") { inclusive = true }
            }
            return@LaunchedEffect
        }

        // If user does have a token
        try {
            // Make the authentication check on IO dispatcher (a background thread,
            // so that it does not hold up the UI)
            val result = withContext(Dispatchers.IO) {
                authRepository.checkAuth(token)
            }

            // If user does not have the required role or has an invalid token
            if (result.isFailure || result.getOrNull()?.role != requiredRole) {
                Toast.makeText(navController.context, "Access denied",
                                Toast.LENGTH_LONG).show()
                // NOTE: could instead redirect users somewhere else if they have a valid token but wrong role
                navController.navigate("landing") {
                    popUpTo("landing") { inclusive = true }
                }
            }

        } catch (e: Exception) {
            // If an unexpected error occurs, redirect the user to the landing page
            Toast.makeText(navController.context, "Access check failed",
                            Toast.LENGTH_LONG).show()
            navController.navigate("landing") {
                popUpTo("landing") { inclusive = true }
            }
        }
    }

    // Render the wrapped content
    content()
}

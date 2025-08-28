package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.LoginViewModel

/**
 * A composable screen that connects the [LoginPage] UI with the [LoginViewModel].
 * It exposes state from the ViewModel to the UI and passes user interactions
 * back to the ViewModel for handling.
 *
 * @param navController Used for navigating between screens.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    userPrefs: UserPreferences
) {
    // Get the current Android context, used for showing Toast status messages
    val context = LocalContext.current

    // Get the ViewModel to hold the state and logic
    val viewModel: LoginViewModel = viewModel()

    // Render the screen UI with state from the ViewModel
    LoginPage(
        email = viewModel.email,
        password = viewModel.password,
        isLoading = viewModel.isLoading,
        onEmailChange = { viewModel.email = it },
        onPasswordChange = { viewModel.password = it },
        onLoginClick = {
            // Send the login request when the submit button is pressed
            viewModel.login(userPrefs) { success, message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                if (success) {
                    // Navigate based on the user's role after a successful login
                    if (viewModel.userRole == "MEMBER") {
                        // TODO: Navigate to dashboard
                        navController.navigate("dashboard") {
                            // Clear back stack so user can't press back to return here
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        // TODO: Navigate to the club members overview
                        navController.navigate("landing") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        },
        onNavigateToSignUp = { navController.navigate("signup") }
    )
}

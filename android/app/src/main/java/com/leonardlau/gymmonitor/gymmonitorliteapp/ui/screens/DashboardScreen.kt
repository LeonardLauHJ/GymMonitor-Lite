package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.DashboardViewModel
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first

/**
 * A composable screen that connects the [DashboardPage] UI with the [DashboardViewModel].
 * It exposes state from the ViewModel to the UI and passes user interactions
 * back to the ViewModel for handling.
 * This dashboard should be accessible to MEMBER users only, which can be enforced by
 * calling this inside of a ProtectedScreen wrapper.
 *
 * @param navController Used for navigating between screens.
 * @param userPrefs UserPreferences for handling locally stored authentication tokens
 */
@Composable
fun DashboardScreen(
    navController: NavController,
    userPrefs: UserPreferences
) {
    // Get the ViewModel to hold the state and logic
    val viewModel: DashboardViewModel = viewModel()

    // Run once DashboardScreen is first displayed
    LaunchedEffect(Unit) {
        // Get the token of the currently authenticated user
        val token = userPrefs.token.first()
        if (token.isNotBlank()) {
            // Load the dashboard data for the user the token belongs to
            // This will update the viewModel's state values
            viewModel.loadDashboard(token)
        } else {
            // If no token, then the user is not logged in.
            navController.navigate("landing") {
                popUpTo("landing") { inclusive = true }
            }
        }
    }

    // Render the screen UI with state from the ViewModel
    DashboardPage(
        dashboard = viewModel.dashboardData,
        isLoading = viewModel.isLoading,
        errorMessage = viewModel.errorMessage
    )
}

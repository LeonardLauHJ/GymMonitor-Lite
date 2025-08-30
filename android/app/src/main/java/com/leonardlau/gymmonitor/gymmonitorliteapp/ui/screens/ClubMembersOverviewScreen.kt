package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.ClubMembersOverviewViewModel
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first

/**
 * A composable screen that connects the [ClubMembersOverviewPage] UI with the [ClubMembersOverviewViewModel].
 * It exposes state from the ViewModel to the UI and passes user interactions
 * back to the ViewModel for handling.
 * This screen should be accessible to STAFF users only, which can be enforced by
 * calling this inside of a ProtectedScreen wrapper.
 *
 * @param navController Used for navigating between screens.
 * @param userPrefs UserPreferences for handling locally stored authentication tokens
 */
@Composable
fun ClubMembersOverviewScreen(
    navController: NavController,
    userPrefs: UserPreferences
) {
    // Get the ViewModel to hold the state and logic
    val viewModel: ClubMembersOverviewViewModel = viewModel()

    // Run once ClubMembersOverviewScreen is first displayed
    LaunchedEffect(Unit) {
        // Get the token of the currently authenticated user
        val token = userPrefs.token.first()
        if (token.isNotBlank()) {
            // Load the clubMembersOverview data for the user the token belongs to
            // This will update the viewModel's state values
            viewModel.loadOverview(token)
        } else {
            // If no token, then the user is not logged in.
            navController.navigate("landing") {
                popUpTo("landing") { inclusive = true }
            }
        }
    }

    // Render the screen UI with state from the ViewModel
    ClubMembersOverviewPage(
        clubMembersOverview = viewModel.overviewData,
        isLoading = viewModel.isLoading,
        errorMessage = viewModel.errorMessage
    )
}

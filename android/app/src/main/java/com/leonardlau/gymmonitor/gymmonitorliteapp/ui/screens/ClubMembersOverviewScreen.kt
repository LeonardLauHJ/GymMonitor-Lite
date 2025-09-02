package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.MemberDrawer
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.StaffDrawer
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.ClubMembersOverviewViewModel
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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

    // Drawer menu state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

    // Wrap the screen inside a ModalNavigationDrawer, which allows us to show
    // a side navigation drawer that can slide in and out.
    ModalNavigationDrawer(
        drawerState = drawerState, // Controls whether the drawer is open or closed
        drawerContent = {
            // Drawer content is defined in StaffDrawer
            StaffDrawer(
                onNavigateClubMembers = { navController.navigate("clubMembersOverview") },
                onLogout = {
                    scope.launch {
                        // Clear the current authentication token
                        userPrefs.clearToken()
                        // Redirect to landing page and remove history
                        navController.navigate("landing") {
                            popUpTo("landing") { inclusive = true }
                        }
                    }
                },
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        // Render the screen UI with state from the ViewModel
        ClubMembersOverviewPage(
            clubMembersOverview = viewModel.overviewData,
            isLoading = viewModel.isLoading,
            errorMessage = viewModel.errorMessage,
            onOpenDrawer = { scope.launch { drawerState.open() } }
        )
    }
}

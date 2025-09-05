package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.MemberDrawer
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.StaffDrawer
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.ClassDetailsViewModel
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.DashboardViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * A composable screen that connects the [ClassDetailsPage] UI with the [ClassDetailsViewModel].
 * It exposes state from the ViewModel to the UI and passes user interactions
 * back to the ViewModel for handling.
 * This screen should be accessible to MEMBER users only, which can be enforced by
 * calling this inside of a ProtectedScreen wrapper.
 *
 * @param classId The id of the class to view details of.
 * @param navController Used for navigating between screens.
 * @param userPrefs UserPreferences for handling locally stored authentication tokens.
 */
@Composable
fun ClassDetailsScreen(
    classId: Int,
    navController: NavController,
    userPrefs: UserPreferences
) {
    // Get the current Android context, used for showing Toast status messages
    val context = LocalContext.current

    // Get the ViewModel to hold the state and logic
    val viewModel: ClassDetailsViewModel = viewModel()

    // Drawer menu state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Run once ClassDetailsScreen is first displayed
    LaunchedEffect(Unit) {
        // Get the token of the currently authenticated user
        val token = userPrefs.token.first()
        if (token.isNotBlank()) {
            // Load the gym class details data
            // This will update the viewModel's state values
            viewModel.loadClassDetails(classId, token)

            // Check the user's role and update the userRole variable accordingly
            viewModel.checkUserRole(token)
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
            // Since this screen is accessible by both MEMBER and STAFF users,
            // check the user's role and ensure the correct drawer is used
            if (viewModel.userRole == "STAFF") {
                // Drawer content is defined in StaffDrawer
                StaffDrawer(
                    onNavigateClubMembers = { navController.navigate("clubMembersOverview") },
                    onNavigateStaffSchedule = { navController.navigate("staffSchedule") },
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
            } else {
                // Drawer content is defined in MemberDrawer
                MemberDrawer(
                    onNavigateDashboard = { navController.navigate("dashboard") },
                    onNavigateTimetable = { navController.navigate("timetable") },
                    onNavigateMembership = { navController.navigate("membership") },
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
        }
    ) {
        // Render the screen UI with state from the ViewModel
        ClassDetailsPage(
            classDetails = viewModel.classDetails,
            userRole = viewModel.userRole,
            isDataLoading = viewModel.isLoading,
            isBookingInProgress = viewModel.isBookingInProgress,
            canBook = viewModel.canBook,
            errorMessage = viewModel.errorMessage,
            onBookClassClick = {
                // Only proceed if classDetails is not null
                viewModel.classDetails?.let { details ->
                    // Launch a coroutine to read the token from UserPreferences
                    scope.launch {
                        val token = userPrefs.token.first()

                        viewModel.bookClass(details.id, token) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                            if (success) {
                                // If the class was successfully booked, update canBook to
                                // false to remove the Book Class button from view
                                viewModel.markClassAsBooked()
                            }
                        }
                    }
                }
            },
            onOpenDrawer = { scope.launch { drawerState.open() } }
        )
    }
}
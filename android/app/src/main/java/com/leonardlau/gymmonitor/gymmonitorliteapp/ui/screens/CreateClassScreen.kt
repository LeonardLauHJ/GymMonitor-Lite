package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.widget.Toast
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.StaffDrawer
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.CreateClassViewModel
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.SignUpViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * A composable screen that connects the [CreateClassPage] UI with the [CreateClassViewModel].
 * It exposes state from the ViewModel to the UI and passes user interactions
 * back to the ViewModel for handling.
 *
 * @param navController Used for navigating between screens.
 * @param userPrefs UserPreferences for handling locally stored authentication tokens.
 */
@Composable
fun CreateClassScreen(
    navController: NavController,
    userPrefs: UserPreferences
) {
    // Get the current Android context, used for showing Toast status messages
    val context = LocalContext.current

    // Get the ViewModel to hold the state and logic
    val viewModel: CreateClassViewModel = viewModel()

    // Drawer menu state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Remember the user's JWT token, will need to send it when the form is submitted
    var token by remember { mutableStateOf("") }

    // Run once CreateClassScreen is first displayed
    LaunchedEffect(Unit) {
        // Get the token of the currently authenticated user
        token = userPrefs.token.first()
        if (!token.isNotBlank()) {
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
                onNavigateStaffSchedule = { navController.navigate("staffSchedule") },
                onNavigateCreateClass = { navController.navigate("createClass") },
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
        CreateClassPage(
            locationId = viewModel.locationId,
            name = viewModel.name,
            description = viewModel.description,
            startTime = viewModel.startTime,
            endTime = viewModel.endTime,
            maxCapacity = viewModel.maxCapacity,
            isLoading = viewModel.isLoading,
            onLocationIdChange = { viewModel.locationId = it },
            onNameChange = { viewModel.name = it },
            onDescriptionChange = { viewModel.description = it },
            onStartTimeChange = { viewModel.startTime = it },
            onEndTimeChange = { viewModel.endTime = it },
            onMaxCapacityChange = { viewModel.maxCapacity = it },
            onSubmitClick = {
                // Call the viewmodel's create class function with the current user's JWT token
                // when the button is pressed
                // If successful, response will be the id of the created class as a string (e.g. "1").
                // Otherwise it will be an error message.
                viewModel.createClass(token) { success, response ->
                    if (success) {
                        // If successful, navigate to the details page of the created class
                        navController.navigate("classDetails/$response")
                    } else {
                        // Otherwise display the error message in a Toast
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show()
                    }
                }
            },
            onOpenDrawer = { scope.launch { drawerState.open() } }
        )
    }
}

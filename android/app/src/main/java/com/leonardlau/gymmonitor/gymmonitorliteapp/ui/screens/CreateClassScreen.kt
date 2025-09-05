package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Calendar

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

    /**
     * Opens a date picker followed by a time picker dialog, and returns the selected date and time.
     *
     * This function first shows a [DatePickerDialog] for the user to pick a year, month, and day.
     * Once a date is selected, a [TimePickerDialog] is shown for the user to pick hours and minutes.
     * The selected date and time are then combined into a [LocalDateTime] object and passed to [onSelected].
     *
     * @param onSelected Callback invoked with the [LocalDateTime] chosen by the user.
     */
    fun openDateTimePicker(onSelected: (LocalDateTime) -> Unit) {
        // Get the current date and time to use as defaults in the pickers
        val calendar = Calendar.getInstance()

        // Show a DatePickerDialog for the user to select year, month, and day
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Once the user selects a date, show a TimePickerDialog for hour and minute
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        // Combine the selected date and time into a LocalDateTime
                        val selected = LocalDateTime.of(year, month + 1, dayOfMonth, hour, minute)
                        // Pass the selected LocalDateTime to the callback
                        onSelected(selected)
                    },
                    // use current date/time as default
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false // use 12-hour format
                ).show()
            },
            // use current date/time as default
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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
            startTimeDisplay = viewModel.startTimeDisplay,
            endTimeDisplay = viewModel.endTimeDisplay,
            maxCapacity = viewModel.maxCapacity,
            isLoading = viewModel.isLoading,
            onLocationIdChange = { viewModel.locationId = it },
            onNameChange = { viewModel.name = it },
            onDescriptionChange = { viewModel.description = it },
            onStartTimeClick = { openDateTimePicker { viewModel.setStartTime(it) } },
            onEndTimeClick = { openDateTimePicker { viewModel.setEndTime(it) } },
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

package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.HeaderWithMenu
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.NumberInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.SubmitButton
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.TextInputField

/**
 * UI composable for the create new gym class screen.
 * Only contains UI. State and logic need to be provided from a ViewModel.
 *
 * @param locationId Current location ID input value
 * @param name Current class name input value
 * @param description Current class description input value
 * @param startTime Current start time input value (e.g. ISO string or formatted date/time)
 * @param endTime Current end time input value
 * @param maxCapacity Current maximum capacity input value
 * @param isLoading Whether a class creation request is currently in progress
 * @param onLocationIdChange Callback when the location ID input changes
 * @param onNameChange Callback when the class name input changes
 * @param onDescriptionChange Callback when the class description input changes
 * @param onStartTimeChange Callback when the start time input changes
 * @param onEndTimeChange Callback when the end time input changes
 * @param onMaxCapacityChange Callback when the max capacity input changes
 * @param onSubmitClick Callback when the Create Class button is clicked to submit the form
 * @param onOpenDrawer Callback triggered when the header menu button is pressed.
 */
@Composable
fun CreateClassPage(
    locationId: String,
    name: String,
    description: String,
    startTime: String,
    endTime: String,
    maxCapacity: String,
    isLoading: Boolean,
    onLocationIdChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onMaxCapacityChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Header section with screen title
        // and menu icon to open/close the navigation drawer menu
        HeaderWithMenu(
            title = "Create a New Class",
            onMenuClick = onOpenDrawer
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Input fields
            TextInputField(
                value = name,
                onValueChange = onNameChange,
                label = "Class Name",
                modifier = Modifier.fillMaxWidth()
            )

            TextInputField(
                value = description,
                onValueChange = onDescriptionChange,
                label = "Description",
                modifier = Modifier.fillMaxWidth()
            )

            // TODO: currently you need to enter a timestamp, need to get a date/time picker
            TextInputField(
                value = startTime,
                onValueChange = onStartTimeChange,
                label = "Start Time",
                modifier = Modifier.fillMaxWidth()
            )

            TextInputField(
                value = endTime,
                onValueChange = onEndTimeChange,
                label = "End Time",
                modifier = Modifier.fillMaxWidth()
            )

            NumberInputField(
                value = maxCapacity,
                onValueChange = onMaxCapacityChange,
                label = "Max Capacity",
                modifier = Modifier.fillMaxWidth()
            )

            NumberInputField(
                value = locationId,
                onValueChange = onLocationIdChange,
                label = "Location ID",
                modifier = Modifier.fillMaxWidth()
            )

            // Submit button
            SubmitButton(
                text = "Create Class",
                isLoading = isLoading,
                onClick = onSubmitClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(3f))
    }
}

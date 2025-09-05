package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.CreateClassRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.ClassRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * ViewModel for the Create Class screen.
 * Handles creating new gym classes with the form data and storing state.
 */
class CreateClassViewModel (
    private val classRepository: ClassRepository = ClassRepository()
) : ViewModel() {
    // State variables for form fields
    var locationId by mutableStateOf("")
    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var maxCapacity by mutableStateOf("")

    // startTime and endTime needs two different variables
    // These are the actual values that will be kept in the background and send to the endpoint
    // They need to be in timestamp format
    var startTime by mutableStateOf("")
    var endTime by mutableStateOf("")
    // These are the values to display to the user on the UI, in a readable format
    // (DD Month YYYY, xx:xx AM/PM)
    var startTimeDisplay by mutableStateOf("")
    var endTimeDisplay by mutableStateOf("")

    // State variable to track if a signup request is currently in progress
    var isLoading by mutableStateOf(false)
        private set // anyone can read, only this class can change

    // Latest error message (null if no error)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Sets the start time for the class.
     * Stores both:
     * - The timestamp string ("YYYY-MM-DDTHH:mm:ss") for backend API use.
     * - A human-readable string (DD Month YYYY, xx:xx AM/PM) for display in the UI.
     *
     * @param selected The [LocalDateTime] chosen by the user from the date/time picker.
     */
    fun setStartTime(selected: LocalDateTime) {
        startTime = selected.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) // backend
        startTimeDisplay = selected.format(DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a")) // user
    }

    /**
     * Sets the end time for the class.
     * Stores both:
     * - The timestamp string ("YYYY-MM-DDTHH:mm:ss") for backend API use.
     * - A human-readable string (DD Month YYYY, xx:xx AM/PM) for display in the UI.
     *
     * @param selected The [LocalDateTime] chosen by the user from the date/time picker.
     */
    fun setEndTime(selected: LocalDateTime) {
        endTime = selected.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) // backend
        endTimeDisplay = selected.format(DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a"))
    }

    /**
     * Attempts to create a new gym class with the current form data.
     *
     * @param token    Authentication token of the staff user creating the class.
     * @param onResult Callback to notify the UI of whether the function succeeded or failed,
     *                 along with the id of the created class if successful, otherwise an error message.
     *                 e.g. onResult(true, "1") or onResult(false, "error message")
     */
    fun createClass(token: String, onResult: (success: Boolean, message: String) -> Unit) {
        // Ensure that all fields are filled
        if (locationId.isBlank() || name.isBlank() || description.isBlank() || startTime.isBlank() ||
            endTime.isBlank() || maxCapacity.isBlank()
        ) {
            // If not all fields were filled, set the result to be a failure with an error message
            onResult(false, "All fields are required")
            return
        }
        // Try converting locationId into an Int
        val locationIdInt = locationId.toIntOrNull()
        if (locationIdInt == null) {
            onResult(false, "Location ID must be a number")
            return
        }

        // Try converting maxCapacity into an Int
        val maxCapacityInt = maxCapacity.toIntOrNull()
        if (maxCapacityInt == null) {
            onResult(false, "Maximum capacity must be a number")
            return
        }

        // Validate startTime and endTime
        try {
            LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (e: DateTimeParseException) {
            onResult(false, "Start time must be in format yyyy-MM-ddTHH:mm:ss")
            return
        }
        try {
            LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (e: DateTimeParseException) {
            onResult(false, "End time must be in format yyyy-MM-ddTHH:mm:ss")
            return
        }

        // Before making the create class request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Set up the request to the backend
            val request = CreateClassRequest(
                locationId = locationIdInt,
                name = name,
                description = description,
                startTime = startTime,
                endTime = endTime,
                maxCapacity = maxCapacityInt
            )

            // Make the request to the backend and get back the result
            val result = classRepository.createClass(request, token)

            // If the create class request was successful
            result.onSuccess { result ->
                // Set the result to success with the id of the created class
                onResult(true, "${result.id}")
            }.onFailure { e ->
                // Otherwise set the result to failure with the message from backend
                errorMessage = e.message ?: "An unknown error occurred"
                onResult(false, errorMessage!!)
            }

            isLoading = false
        }
    }
}
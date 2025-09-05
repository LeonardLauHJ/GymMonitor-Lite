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
    var startTime by mutableStateOf("")
    var endTime by mutableStateOf("")
    var maxCapacity by mutableStateOf("")

    // State variable to track if a signup request is currently in progress
    var isLoading by mutableStateOf(false)
        private set // anyone can read, only this class can change

    // Latest error message (null if no error)
    var errorMessage by mutableStateOf<String?>(null)
        private set

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
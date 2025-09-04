package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.GymClassDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.ClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the Class Details screen.
 * Handles fetching a single class's details and storing state.
 */
class ClassDetailsViewModel(
    private val classRepository: ClassRepository = ClassRepository(),
    private val authRepository: AuthRepository = AuthRepository(),
) : ViewModel() {

    // The gym class details data returned from the API. Null if not loaded yet
    var classDetails by mutableStateOf<GymClassDetailsResponse?>(null)
        private set // anyone can read, only this class can change

    // True while a network request is in progress
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Error message to display if failed to book a class
    var bookingErrorMessage by mutableStateOf<String?>(null)
        private set

    // Variable to keep track of user's role
    // class details screen is accessible to any authenticated user, but some functions (book class)
    // are only available to certain roles
    var userRole by mutableStateOf<String?>(null)
        private set

    /**
     * Loads the details of the gym class using the given class ID and the provided JWT token.
     * Updates the UI state variables accordingly.
     *
     * @param id ID of the class to fetch
     * @param token JWT token used for authentication
     */
    fun loadClassDetails(id: Int, token: String) {
        // Before making the request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Fetch the gym class details data
            classRepository.getClassDetails(id, token).onSuccess { details ->
                classDetails = details
            }.onFailure { e ->
                // If an error occurs, set the returned error message
                errorMessage = e.message
            }

            isLoading = false
        }
    }

    /**
     * Checks the role of the currently authenticated user, and sets the value to userRole.
     *
     * @param token The authentication token of the currently logged-in user.
     */
    fun checkUserRole(token: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                authRepository.checkAuth(token)
            }
            if (result.isSuccess) {
                userRole = result.getOrNull()?.role
            }
        }
    }

    /**
     * Attempts to book the class with the given [id] for the user with the given [token].
     *
     * @param id       ID of the class to book.
     * @param token    Authentication token of the user to book the class for.
     * @param onResult Callback to notify the UI of whether the function succeeded or failed,
     *                 along with a message to display.
     *                 e.g. onResult(boolean, "example message")
     *                 where boolean is true if the function succeeded, and false if it failed.
     */
    fun bookClass(id: Int, token: String, onResult: (success: Boolean, message: String) -> Unit) {
        viewModelScope.launch {
            bookingErrorMessage = null

            // Make the request to the backend and get back the result
            val result = classRepository.bookClass(id, token)

            // If the booking was successful
            result.onSuccess { result ->
                // Set the result to success with the message from backend
                onResult(true, result)
            }.onFailure { e ->
                // Otherwise set the result to failure with the message from backend if given
                bookingErrorMessage = e.message
                onResult(false, bookingErrorMessage!!)
            }
        }
    }
}
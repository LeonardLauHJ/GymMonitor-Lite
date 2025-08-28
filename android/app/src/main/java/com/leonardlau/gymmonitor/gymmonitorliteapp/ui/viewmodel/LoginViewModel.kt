package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // State variables for form fields
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // State variable to track if a signup request is currently in progress
    var isLoading by mutableStateOf(false)
        private set // anyone can read, only this class can change

    // Latest error message (null if no error)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Success message returned from backend (null if not signed up yet)
    var successMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Attempts to log in the user with the current form data.
     *
     * @param onResult Callback to notify the UI of whether the function succeeded or failed,
     *                 along with a message to display.
     *                 e.g. onResult(boolean, "example message")
     *                 where boolean is true if the function succeeded, and false if it failed.
     */
    fun login(userPrefs: UserPreferences, onResult: (success: Boolean, message: String) -> Unit) {
        // Ensure that all fields are filled
        if (email.isBlank() || password.isBlank()) {
            // If not all fields were filled, set the result to be a failure with an error message
            onResult(false, "All fields are required")
            return
        }

        // Before making the login request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null

            // Set up the request to the backend
            val request = LoginRequest(email, password)

            // Make the request to the backend and get back the result
            val result = repository.login(request)

            // If the login request was successful
            result.onSuccess { token ->
                // take the JWT token from the result and save it locally
                userPrefs.saveToken(token)
                // Set the result to a success with a success message
                onResult(true, "Login Successful")
            }.onFailure { e ->
                // Otherwise set the result to failure with the message from backend if given
                errorMessage = e.message ?: "An unknown error occurred"
                onResult(false, errorMessage!!)
            }

            isLoading = false
        }
    }
}
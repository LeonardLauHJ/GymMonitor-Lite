package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

/**
 * ViewModel containing the state and logic for the SignUp screen.
 *
 * @property repository The repository to handle requests to the backend API.
 */
class SignUpViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // State variables for form fields
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var clubCode by mutableStateOf("")
    var membershipPlanId by mutableStateOf("")

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
     * Attempts to sign up the user with the current form data.
     *
     * @param onResult Callback to notify the UI of whether the function succeeded or failed,
     *                 along with a message to display.
     *                 e.g. onResult(boolean, "example message")
     *                 where boolean is true if the function succeeded, and false if it failed.
     */
    fun signUp(onResult: (success: Boolean, message: String) -> Unit) {
        // Ensure that all fields are filled
        if (name.isBlank() || email.isBlank() || password.isBlank() ||
            clubCode.isBlank() || membershipPlanId.isBlank()
        ) {
            // If not all fields were filled, set the result to be a failure with an error message
            onResult(false, "All fields are required")
            return
        }

        // Try converting membershipPlanId into an Int
        val planId = membershipPlanId.toIntOrNull()
        if (planId == null) {
            onResult(false, "Membership Plan ID must be a number")
            return
        }

        // Before making the signup request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null

            // Set up the request to the backend
            val request = SignupRequest(name, email, password, clubCode, planId)

            // Make the request to the backend and get back the result
            val result = repository.signup(request)

            // If the signup request was successful
            result.onSuccess { msg ->
                // Set the result to success with the message from backend
                successMessage = msg
                onResult(true, msg)
            }.onFailure { e ->
                // Otherwise set the result to failure with the message from backend
                errorMessage = e.message ?: "An unknown error occurred"
                onResult(false, errorMessage!!)
            }

            isLoading = false
        }
    }
}

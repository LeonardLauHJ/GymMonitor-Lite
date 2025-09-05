package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MembershipDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.MembershipRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Membership Details screen.
 * Handles fetching the currently authenticated member's membership details and storing state.
 */
class MembershipDetailsViewModel (
    private val membershipRepository: MembershipRepository = MembershipRepository()
    ) : ViewModel() {

    // The membership details data returned from the API. Null if not loaded yet
    var membershipDetails by mutableStateOf<MembershipDetailsResponse?>(null)
        private set // anyone can read, only this class can change

    // True while a network request is in progress
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Loads the details of the user's membership using their JWT auth token.
     * Updates the UI state variables accordingly.
     *
     * @param token JWT token belonging to the user to retrieve membership details for.
     */
    fun loadMembershipDetails(token: String) {
        // Before making the request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Fetch the membership details data
            membershipRepository.getMembershipDetails(token).onSuccess { details ->
                membershipDetails = details
            }.onFailure { e ->
                // If an error occurs, set the returned error message
                errorMessage = e.message
            }

            isLoading = false
        }
    }
}
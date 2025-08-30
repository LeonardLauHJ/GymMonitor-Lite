package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MemberOverview
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.ClubMembersOverviewRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the club members overview screen which is shown to staff members.
 * Responsible for fetching the list of member overview data and storing state for it.
 *
 * @property clubMembersOverviewRepository Handles fetching club members overview data from the API
 */
class ClubMembersOverviewViewModel(
    private val clubMembersOverviewRepository: ClubMembersOverviewRepository = ClubMembersOverviewRepository()
) : ViewModel() {

    // The list of club members overview data returned from the API. Empty list if not loaded yet
    var overviewData by mutableStateOf<List<MemberOverview>>(emptyList())
        private set // anyone can read, only this class can change

    // True while a network request is in progress
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Loads the club members overview data using the provided JWT token.
     * Updates the UI state variables accordingly.
     *
     * @param token JWT token used for authentication in API calls
     */
    fun loadOverview(token: String) {
        // Before making the request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Fetch the club members overview data
            clubMembersOverviewRepository.getClubMembersOverview(token).onSuccess { memberOverview ->
                overviewData = memberOverview
            }.onFailure { e ->
                // If an error occurs, set the returned error message
                errorMessage = e.message
            }

            isLoading = false
        }
    }
}

package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.DashboardResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.CheckAuthResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.DashboardRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Member Dashboard screen.
 * Responsible for fetching dashboard data for members and storing state for it.
 *
 * @property dashboardRepository Handles fetching dashboard data from the API
 */
class DashboardViewModel(
    private val dashboardRepository: DashboardRepository = DashboardRepository()
) : ViewModel() {

    // The dashboard data returned from the API. Null if not loaded yet
    var dashboardData by mutableStateOf<DashboardResponse?>(null)
        private set // anyone can read, only this class can change

    // True while a network request is in progress
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Loads the member dashboard data using the provided JWT token.
     * Updates the UI state variables accordingly.
     *
     * @param token JWT token used for authentication in API calls
     */
    fun loadDashboard(token: String) {
        // Before making the request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Fetch the dashboard data
            dashboardRepository.getDashboard(token).onSuccess { dashboard ->
                dashboardData = dashboard
            }.onFailure { e ->
                // If an error occurs, set the returned error message
                errorMessage = e.message
            }

            isLoading = false
        }
    }
}

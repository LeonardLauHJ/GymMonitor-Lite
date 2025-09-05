package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.StaffScheduleEntry
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.TimetableEntry
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.StaffScheduleRepository
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.TimetableRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the staff schedule screen, which shows off all upcoming classes being instructed
 * by the currently authenticated staff user.
 * Responsible for fetching the list of schedule entries and storing state for it.
 *
 * @property StaffScheduleRepository Handles fetching the list of schedule entries
 */
class StaffScheduleViewModel(
    private val staffScheduleRepository: StaffScheduleRepository = StaffScheduleRepository()
) : ViewModel() {

    // The list of schedule entries returned from the API. Empty list if not loaded yet
    var scheduleData by mutableStateOf<List<StaffScheduleEntry>>(emptyList())
        private set // anyone can read, only this class can change

    // True while a network request is in progress
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Loads the full schedule data for the staff user with the provided JWT token.
     * Updates the UI state variables accordingly.
     *
     * @param token JWT token used for authentication in API calls
     */
    fun loadFullSchedule(token: String) {
        // Before making the request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Fetch the full schedule data for the staff user who owns the JWT token
            staffScheduleRepository.getFullSchedule(token).onSuccess { scheduleEntries ->
                scheduleData = scheduleEntries
            }.onFailure { e ->
                // If an error occurs, set the returned error message
                errorMessage = e.message
            }

            isLoading = false
        }
    }
}

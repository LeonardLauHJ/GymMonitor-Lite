package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.TimetableEntry
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.TimetableRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the timetable screen, which shows off all upcoming classes at the member's club.
 * Responsible for fetching the list of timetable entries and storing state for it.
 *
 * @property TimetableRepository Handles fetching the list of timetable entries
 */
class TimetableViewModel(
    private val timetableRepository: TimetableRepository = TimetableRepository()
) : ViewModel() {

    // The list of timetable entries returned from the API. Empty list if not loaded yet
    var timetableData by mutableStateOf<List<TimetableEntry>>(emptyList())
        private set // anyone can read, only this class can change

    // True while a network request is in progress
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Loads the timetable data using the provided JWT token.
     * Updates the UI state variables accordingly.
     *
     * @param token JWT token used for authentication in API calls
     */
    fun loadTimetable(token: String) {
        // Before making the request, first launch a coroutine in the ViewModel scope,
        // this will let us run background tasks safely without memory leaks
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Fetch the timetable data
            timetableRepository.getFullTimetable(token).onSuccess { timetableEntries ->
                timetableData = timetableEntries
            }.onFailure { e ->
                // If an error occurs, set the returned error message
                errorMessage = e.message
            }

            isLoading = false
        }
    }
}

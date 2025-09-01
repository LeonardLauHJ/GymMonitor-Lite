package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.TimetableEntry
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.ClubMemberOverviewCard
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.PageHeader

/**
 * UI composable for the Club Timetable screen.
 * Only contains UI. State and logic need to be provided from a ViewModel.
 *
 * @param timetableEntries The list of timetable entries to display. Empty if not yet loaded.
 * @param isLoading Whether the data is currently loading.
 * @param errorMessage Error message to display if something went wrong.
 */
@Composable
fun TimetablePage(
    timetableEntries: List<TimetableEntry> = emptyList(),
    isLoading: Boolean,
    errorMessage: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            // Display a loading spinner if the data has not yet been loaded,
            // as it gets fetched asynchronously in the background.
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // If the backend call fails, display an error message
            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // If we have the timetable data loaded
            timetableEntries.isNotEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFF0ECF8)),
                ) {

                    // Header Section
                    PageHeader(text = "Club Timetable")

                    // List of club members with a summary of their information
                    // LazyColumn will only render items that are on-screen
                    // (if list extends past the screen the off-screen ones aren't loaded)
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 5.dp),
                    ) {
                        items(timetableEntries) { entry ->
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = entry.className)
                                Text(text = entry.instructorName)
                                Text(text = entry.locationName)
                                Text(text = entry.startTime)
                                Text(text = "${entry.durationMinutes}")
                                Text(text = "${entry.currentBookings}")
                                Text(text = "${entry.maxCapacity}")
                            }
                        }

                    }

                }
            }

            // If the member user's club has no upcoming classes
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFF0ECF8)),
                ) {
                    PageHeader(text = "Club Timetable")
                    Text(
                        text = "Your club currently has no upcoming classes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
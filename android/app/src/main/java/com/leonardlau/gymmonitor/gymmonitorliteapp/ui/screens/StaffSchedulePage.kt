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
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.StaffScheduleEntry
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.HeaderWithMenu
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.PageHeader
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.StaffScheduleEntryItem
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.SoftGray

/**
 * UI composable for the Staff Schedule screen.
 * Only contains UI. State and logic need to be provided from a ViewModel.
 *
 * @param scheduleEntries The list of schedule entries to display. Empty if not yet loaded.
 * @param isLoading Whether the data is currently loading.
 * @param errorMessage Error message to display if something went wrong.
 * @param onOpenDrawer Callback triggered when the header menu button is pressed
 *                     to open the drawer, e.g. `scope.launch { drawerState.open() }`.
 */
@Composable
fun StaffSchedulePage(
    scheduleEntries: List<StaffScheduleEntry> = emptyList(),
    isLoading: Boolean,
    errorMessage: String?,
    onOpenDrawer: () -> Unit,
    onClassClick: (Int) -> Unit
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

            // If we have the schedule data loaded
            scheduleEntries.isNotEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = SoftGray),
                ) {

                    // Header section with screen title
                    // and menu icon to open/close the navigation drawer menu
                    HeaderWithMenu(
                        title = "Class Schedule",
                        onMenuClick = onOpenDrawer
                    )

                    // List of classes in the Staff user's schedule
                    // LazyColumn will only render items that are on-screen
                    // (if list extends past the screen the off-screen ones aren't loaded)
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        items(scheduleEntries) { entry ->
                            StaffScheduleEntryItem(
                                scheduleEntry = entry,
                                onClick = {
                                    // Clicking the entry navigates to the specific class' details page
                                    onClassClick(entry.classId)
                                }
                            )
                        }

                    }

                }
            }

            // If the staff user has no upcoming classes they need to instruct
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = SoftGray),
                ) {
                    PageHeader(text = "Class Schedule")
                    Text(
                        text = "You do not have any upcoming classes to instruct.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
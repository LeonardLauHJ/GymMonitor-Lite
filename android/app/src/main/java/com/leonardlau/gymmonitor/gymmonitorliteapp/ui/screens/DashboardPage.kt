package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.BookingSummary
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.DashboardResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.DashboardStat
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.ScreenTitle
import java.time.format.DateTimeFormatter

/**
 * UI composable for the member dashboard.
 * Displays dashboard data including total visits, bookings, amount owed, and upcoming bookings.
 *
 * @param dashboard The dashboard data to display. If null, shows empty state.
 * @param isLoading Whether the data is currently loading.
 * @param errorMessage Error message to display if something went wrong.
 */
@Composable
fun DashboardPage(
    dashboard: DashboardResponse?,
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

            // If we have the dashboard data
            dashboard != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Header section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.2f) // Cover the top 20% of the screen
                            .background(color = Color(0xFF495D91)),
                        contentAlignment = Alignment.Center // Center the title text
                    ) {
                        ScreenTitle(
                            text = dashboard.dashboardTitle,
                            color = Color.White // use white for contrast
                        )
                    }

                    // Stats row (Number of Bookings, Visits, and the Amount Owed)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, bottom = 25.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        DashboardStat(
                            label = "Bookings",
                            value = dashboard.totalBookings.toString()
                        )
                        DashboardStat(
                            label = "Visits",
                            value = dashboard.totalVisits.toString()
                        )
                        DashboardStat(
                            label = "Amount Owed",
                            value = dashboard.amountOwed
                        )
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(dashboard.upcomingBookings) { booking ->
                            // Parse startTime manually into a readable format
                            // (Alternative methods such as LocalDateTime.parse may not be supported by older android versions)
                            val formattedTime = try {
                                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                                val date = sdf.parse(booking.startTime) // returns java.util.Date
                                if (date != null) {
                                    java.text.SimpleDateFormat("hh:mma dd/MM/yyyy", java.util.Locale.getDefault()).format(date)
                                } else {
                                    "Invalid date"
                                }
                            } catch (e: Exception) {
                                booking.startTime // if parsing fails, just show the start time in its raw format
                            }

                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = booking.className, fontWeight = FontWeight.Bold)
                                Text(text = "Location: ${booking.locationName}")
                                Text(text = "Starts: $formattedTime")
                                Text(text = "Duration: ${booking.durationMinutes} mins")
                            }
                        }
                    }
                }
            }
        }
    }
}
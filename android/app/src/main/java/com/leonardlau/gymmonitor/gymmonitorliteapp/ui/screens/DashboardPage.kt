package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.DashboardResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.DashboardStat
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.ScreenTitle
import com.leonardlau.gymmonitor.gymmonitorliteapp.R
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.BookingCard
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.HeaderWithMenu
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.PrimaryBlue
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.SoftGray

/**
 * UI composable for the member dashboard.
 * Displays dashboard data including total visits, bookings, amount owed, and upcoming bookings.
 *
 * @param dashboard The dashboard data to display, null if not yet loaded.
 * @param isLoading Whether the data is currently loading.
 * @param errorMessage Error message to display if something went wrong.
 * @param onOpenDrawer Callback triggered when the header menu button is pressed
 *                     to open the drawer, e.g. `scope.launch { drawerState.open() }`.
 * @param onClassClick Callback triggered when an upcoming booking is clicked, receives the class ID
 *                     of the clicked entry. Should navigate to the details page for that class.
 */
@Composable
fun DashboardPage(
    dashboard: DashboardResponse?,
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

            // If we have the dashboard data
            dashboard != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    // Header section with screen title
                    // and menu icon to open/close the navigation drawer menu
                    HeaderWithMenu(
                        title = dashboard.dashboardTitle,
                        onMenuClick = onOpenDrawer
                    )

                    // Header section and Profile Picture
                    Box {
                        // Extend the header background color
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                                .background(color = PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                        }

                        // Profile picture, half overlapping the header background
                        Image(
                            // Profile pictures not yet supported by backend, just use a default
                            painter = painterResource(id = R.drawable.default_profile_icon),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(130.dp)
                                .align(Alignment.BottomCenter) // Bottom center of outer Box
                                .clip(CircleShape), // Turn the image into a circle shape
                            contentScale = ContentScale.Crop // Scale to fill container, crop overflow
                        )
                    }

                    // Stats row (Number of Bookings, Visits, and the Amount Owed)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 25.dp, bottom = 20.dp),
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

                    // Upcoming Bookings section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.LightGray)
                    ) {
                        // Section title
                        Text(
                            text = "Upcoming Bookings",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        )
                        // Upcoming class bookings list
                        // LazyColumn will only render items that are on-screen
                        // (if list extends past the screen the off-screen ones aren't loaded)
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            // If there are any bookings to display
                            if (dashboard.upcomingBookings.isNotEmpty()) {
                                // Create a BookingCard item for each of the upcoming bookings
                                items(dashboard.upcomingBookings) { booking ->
                                    BookingCard(
                                        bookingSummary = booking,
                                        onClick = {
                                            // Function to navigate to the specific class' details page
                                            onClassClick(booking.classId)
                                        }
                                    )
                                }
                            } else {
                                item {
                                    Text(
                                        text = "You have no upcoming bookings",
                                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
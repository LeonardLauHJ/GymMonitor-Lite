package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.Surface
import java.util.Locale

/**
 * A reusable card component which displays a date and time range in a clean and simple format.
 * The card shows:
 * - A calendar icon with the formatted start date (e.g. "Thursday 02/01/2025") and a "Date" label
 * - A clock icon with the formatted start and end times (e.g. "09:00AM - 10:30AM") and a "Time" label
 *
 * @param startTime The start time in ISO string format (e.g. "2025-01-02T09:00:00")
 * @param endTime The end time of the class in ISO string format (e.g. "2025-01-02T10:30:00")
 */
@Composable
fun DateAndTimeCard(
    startTime: String,
    endTime: String
) {
    // Parsed values
    var formattedDate = "" // Desired format: "Day DD/MM/YYYY"
    var formattedStartTime = "" // Desired format: hh:mmAM/PM - hh:mmAM/PM
    var formattedEndTime = "" // Desired format: hh:mmAM/PM - hh:mmAM/PM

    try {
        // Remove fractional seconds if present
        val cleanedStart = startTime.substringBefore(".")
        val cleanedEnd = endTime.substringBefore(".")

        // Parse the dates and times manually into the desired format
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val startDate = sdf.parse(cleanedStart)
        val endDate = sdf.parse(cleanedEnd)

        if (startDate != null && endDate != null) {
            formattedDate = SimpleDateFormat("EEEE dd/MM/yyyy", Locale.getDefault()).format(startDate)
            formattedStartTime = SimpleDateFormat("hh:mma", Locale.getDefault()).format(startDate)
            formattedEndTime = SimpleDateFormat("hh:mma", Locale.getDefault()).format(endDate)
        }
    } catch (e: Exception) {
        formattedDate = "Invalid date"
        formattedStartTime = "Unavailable"
        formattedEndTime = ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp), // Shadow under the card
        shape = RectangleShape // no rounded corners (corners are rounded by default)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(color = Surface)
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Date section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Calendar Icon
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar",
                        modifier = Modifier.padding(end = 20.dp)
                                           .size(24.dp)
                    )
                    // Date with label
                    Column {
                        Text(
                            text = formattedDate,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Date",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Time section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Clock Icon
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Clock",
                        modifier = Modifier.padding(end = 20.dp)
                                           .size(24.dp)
                    )
                    // Time range with label
                    Column {
                        Text(
                            text = "$formattedStartTime - $formattedEndTime",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Time",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

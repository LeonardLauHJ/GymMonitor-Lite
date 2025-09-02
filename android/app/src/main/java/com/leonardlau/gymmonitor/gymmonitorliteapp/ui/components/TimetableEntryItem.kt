package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.TimetableEntry
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A reusable component that displays information about a single gym class entry
 * in the club timetable.
 *
 * @param timetableEntry The [TimetableEntry] containing class info to display.
 * @param fontSize The size of the text. Defaults to 17.sp.
 */
@Composable
fun TimetableEntryItem(
    timetableEntry: TimetableEntry,
    fontSize: TextUnit = 17.sp,
) {
    // Date and time variables
    var formattedDate = ""
    var formattedTime = ""

    // Take the startTime timestamp and convert it into date (dd/mm/yy) and time (xx:xxAM/PM)
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(timetableEntry.startTime)
        if (date != null) {
            formattedDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
            formattedTime = SimpleDateFormat("hh:mma", Locale.getDefault()).format(date)
        } else {
            "Invalid date"
        }
    } catch (e: Exception) {
        // if parsing fails
        formattedDate = timetableEntry.startTime
        formattedTime = "Unavailable"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF))
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            // Date, time and duration
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(text = formattedDate, fontSize = fontSize)
                Text(text = formattedTime, fontSize = fontSize)
                Text(text = "${timetableEntry.durationMinutes} Mins", fontSize = fontSize)
            }

            // Class name, instructor name, and location info
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(text = timetableEntry.className, fontWeight = FontWeight.Bold, fontSize = fontSize)
                Text(text = timetableEntry.instructorName, fontSize = fontSize)
                Text(text = timetableEntry.locationName, fontSize = fontSize)
            }

            // Spcaer to push the booking capacity to the right side
            Spacer(modifier = Modifier.weight(1f))

            // Booking capacity
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = "${timetableEntry.currentBookings}/${timetableEntry.maxCapacity}",
                    fontSize = fontSize
                )
            }
        }
    }
}

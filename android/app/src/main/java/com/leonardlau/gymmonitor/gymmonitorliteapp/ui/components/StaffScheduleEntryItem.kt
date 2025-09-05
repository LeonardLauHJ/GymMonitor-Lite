package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.StaffScheduleEntry
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A reusable component that displays information about a single gym class entry
 * in the staff class schedule.
 *
 * @param scheduleEntry The [StaffScheduleEntry] containing class info to display.
 * @param onClick Lambda function to invoke when the entry is clicked, receives the class ID.
 *                Should navigate to the Class Details page for this class.
 * @param fontSize The size of the text. Defaults to 17.sp.
 */
@Composable
fun StaffScheduleEntryItem(
    scheduleEntry: StaffScheduleEntry,
    onClick: (Int) -> Unit,
    fontSize: TextUnit = 17.sp,
) {
    // Date and time variables
    var formattedDate = ""
    var formattedTime = ""

    // Take the startTime timestamp and convert it into date (dd/mm/yy) and time (xx:xxAM/PM)
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(scheduleEntry.startTime)
        if (date != null) {
            formattedDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
            formattedTime = SimpleDateFormat("hh:mma", Locale.getDefault()).format(date)
        } else {
            "Invalid date"
        }
    } catch (e: Exception) {
        // if parsing fails
        formattedDate = scheduleEntry.startTime
        formattedTime = "Unavailable"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick(scheduleEntry.classId) } // invoke the onClick when clicked
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date, time and duration
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(text = formattedDate, fontSize = fontSize)
                Text(text = formattedTime, fontSize = fontSize)
                Text(text = "${scheduleEntry.durationMinutes} Mins", fontSize = fontSize)
            }

            // Class name, instructor name, and location info
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(text = scheduleEntry.className, fontWeight = FontWeight.Bold, fontSize = fontSize)
                Text(text = scheduleEntry.locationName, fontSize = fontSize)
            }

            // Spacer to push the rest to the right side
            Spacer(modifier = Modifier.weight(1f))

            // Booking capacity
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                BookingCapacityRing(
                    current = scheduleEntry.currentBookings,
                    max = scheduleEntry.maxCapacity
                )
            }

            // Arrow Icon to indicate the entry is clickable
            Column() {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Next",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(15.dp)
                )
            }
        }
    }
}

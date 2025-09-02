package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.BookingSummary

/**
 * A reusable component which displays a summary of information about a gym class booking.
 *
 * @param bookingSummary The [BookingSummary] containing the data to display.
 * @param fontSize The size of the text to display. Defaults to 17.sp
 * @param onClick Lambda function to invoke when the entry is clicked, receives the class ID.
 *                Should navigate to the Class Details page for this class.
 */
@Composable
fun BookingCard(
    bookingSummary: BookingSummary,
    onClick: (Int) -> Unit,
    fontSize: androidx.compose.ui.unit.TextUnit = 17.sp,
) {
    // Parse startTime manually into a readable format 
    // (Alternative methods such as LocalDateTime.parse may not be supported by older android versions)
    val formattedTime = try {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
        val date = sdf.parse(bookingSummary.startTime) // returns java.util.Date
        if (date != null) {
            java.text.SimpleDateFormat("hh:mma dd/MM/yy", java.util.Locale.getDefault()).format(date)
        } else {
            "Invalid date"
        }
    } catch (e: Exception) {
        // if parsing fails, just show the start time in its raw format
        bookingSummary.startTime
    }

    Column(
        modifier = Modifier.fillMaxWidth()
                            .background(color = Color.White)
                            .clickable { onClick(bookingSummary.classId) }
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = formattedTime,
                    fontSize = fontSize
                )
                Text(
                    text = "${bookingSummary.durationMinutes} Mins",
                    fontSize = fontSize
                )
            }
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = bookingSummary.className,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                )
                Text(
                    text = bookingSummary.locationName,
                    fontSize = fontSize
                )
            }
        }
    }
}

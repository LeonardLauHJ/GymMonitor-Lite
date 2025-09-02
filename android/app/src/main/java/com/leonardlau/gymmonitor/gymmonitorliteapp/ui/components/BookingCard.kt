package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.BookingSummary
import java.text.SimpleDateFormat
import java.util.Locale

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
    // Date and time variables
    var formattedDate = ""
    var formattedTime = ""

    // Parse startTime manually into a readable format
    // (Alternative methods such as LocalDateTime.parse may not be supported by older android versions)
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(bookingSummary.startTime)
        if (date != null) {
            formattedDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
            formattedTime = SimpleDateFormat("hh:mma", Locale.getDefault()).format(date)
        } else {
            "Invalid date"
        }
    } catch (e: Exception) {
        // if parsing fails
        formattedDate = bookingSummary.startTime
        formattedTime = "Unavailable"
    }

    Column(
        modifier = Modifier.fillMaxWidth()
                            .background(color = Color.White)
                            .clickable { onClick(bookingSummary.classId) }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date, Time, and Duration
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = formattedDate,
                    fontSize = fontSize
                )
                Text(
                    text = formattedTime,
                    fontSize = fontSize
                )
                Text(
                    text = "${bookingSummary.durationMinutes} Mins",
                    fontSize = fontSize
                )
            }

            // Class name and Location
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = bookingSummary.className,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                )

                // Add some space between the name and location
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = bookingSummary.locationName,
                    fontSize = fontSize
                )
            }

            // Spacer to push the rest to the right side
            Spacer(modifier = Modifier.weight(1f))

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

package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Place
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

/**
 * A reusable card component which displays the club and location for a class a clean and simple format.
 * The card shows:
 * - A location pin icon with the club name and a "Club" label
 * - The location of the class with a "Location" label
 *
 * @param club The club to display
 * @param location The location to display
 */
@Composable
fun ClubAndLocationCard(
    club: String,
    location: String
) {
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
                // Club section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Location Pin Icon
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Location",
                        modifier = Modifier.padding(end = 20.dp)
                                           .size(24.dp)
                    )
                    // Club with label
                    Column {
                        Text(
                            text = club,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Club",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Location section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // No Icon, add in the width it would have given
                    Spacer(modifier = Modifier.width(44.dp))

                    // Location with label
                    Column {
                        Text(
                            text = location,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Location",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

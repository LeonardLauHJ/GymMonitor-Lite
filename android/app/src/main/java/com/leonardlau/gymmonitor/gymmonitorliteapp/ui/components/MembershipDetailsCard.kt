package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MembershipDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.Surface
import java.util.Locale

/**
 * A card showing membership details such as club name, join date, total visits,
 * membership plan, next billing date, and amount due.
 *
 * @param membershipDetails Membership details returned from the backend.
 */
@Composable
fun MembershipDetailsCard(
    membershipDetails: MembershipDetailsResponse
) {
    // Formats timestamp to DD/MM/YYYY
    fun formatTimestamp(dateString: String): String {
        return try {
            val cleaned = dateString.substringBefore(".") // remove fractional seconds if present
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val parsedDate = sdf.parse(cleaned)
            if (parsedDate != null) {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsedDate)
            } else {
                "Invalid date"
            }
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    // Formats YYYY-MM-DD to DD/MM/YYYY
    fun formatSimpleDate(dateString: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = sdf.parse(dateString)
            if (parsedDate != null) {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsedDate)
            } else {
                "Invalid date"
            }
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    // Format the Date Joined (timestamp) and Next Billing date (YYYY-MM-DD) to DD/MM/YYYY format
    val formattedDateJoined = formatTimestamp(membershipDetails.dateJoined)
    val formattedNextBilling = formatSimpleDate(membershipDetails.nextBillingDate)


    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp), // Shadow under the card
        shape = RectangleShape // no rounded corners (corners are rounded by default)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(25.dp)
        ) {
            // Club name
            Text(
                text = membershipDetails.clubName,
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Date joined
            Row {
                Text("Joined: ", color = Color.DarkGray)
                Text(formattedDateJoined)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Total visits
            Row {
                Text("Total visits: ", color = Color.DarkGray)
                Text("${membershipDetails.totalVisits}")
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Membership plan
            Row {
                Text("Plan: ", color = Color.DarkGray)
                Text(membershipDetails.membershipPlanName)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Next billing date
            Row {
                Text("Next billing date: ", color = Color.DarkGray)
                Text(formattedNextBilling)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Amount due
            Row {
                Text("Amount due: ", color = Color.DarkGray)
                Text(membershipDetails.amountDue)
            }
        }
    }
}

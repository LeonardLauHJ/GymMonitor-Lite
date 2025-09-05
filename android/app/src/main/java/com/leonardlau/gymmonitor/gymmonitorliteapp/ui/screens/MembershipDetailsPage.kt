package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MembershipDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.HeaderWithMenu

/**
 * UI composable for displaying detailed information about a single gym class.
 *
 * @param membershipDetails The data to display, null if not yet loaded.
 * @param isDataLoading Whether the membership data is currently loading.
 * @param errorMessage Error message to display if something went wrong.
 * @param onOpenDrawer Callback triggered when the header menu button is pressed.
 */
@Composable
fun MembershipDetailsPage(
    membershipDetails: MembershipDetailsResponse?,
    isDataLoading: Boolean,
    errorMessage: String?,
    onOpenDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            // Display a loading spinner if the data has not yet been loaded,
            // as it gets fetched asynchronously in the background.
            isDataLoading -> {
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

            // If we have the membership details data
            membershipDetails != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Header section with screen title
                    // and menu icon to open/close the navigation drawer menu
                    HeaderWithMenu(
                        title = "My Membership",
                        onMenuClick = onOpenDrawer
                    )

                    Text(membershipDetails.clubName)
                    Text(membershipDetails.dateJoined)
                    Text("${ membershipDetails.totalVisits }")
                    Text(membershipDetails.membershipPlanName)
                    Text(membershipDetails.nextBillingDate)
                    Text(membershipDetails.amountDue)
                }
            }
        }
    }
}
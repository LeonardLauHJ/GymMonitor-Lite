package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MemberOverview
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.ClubMemberOverviewCard
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.PageHeader

/**
 * UI composable for the member clubMembersOverview.
 *
 * @param clubMembersOverview The list of MemberOverview data to display. Empty if not yet loaded.
 * @param isLoading Whether the data is currently loading.
 * @param errorMessage Error message to display if something went wrong.
 */
@Composable
fun ClubMembersOverviewPage(
    clubMembersOverview: List<MemberOverview> = emptyList(),
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

            // If we have the clubMembersOverview data
            clubMembersOverview.isNotEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFF0ECF8)),
                ) {

                    // Header Section
                    PageHeader(text = "Club Members")

                    // List of club members with a summary of their information
                    // LazyColumn will only render items that are on-screen
                    // (if list extends past the screen the off-screen ones aren't loaded)
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 5.dp),
                    ) {
                        items(clubMembersOverview) { member ->
                            ClubMemberOverviewCard(member)
                        }

                    }

                }
            }

            // If the club has no members
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFF0ECF8)),
                ) {
                    PageHeader(text = "Club Members")
                    Text(
                        text = "This club currently has no members.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
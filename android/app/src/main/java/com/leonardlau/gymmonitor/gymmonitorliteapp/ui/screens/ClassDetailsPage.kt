package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.R
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.GymClassDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.ClassDetailsBanner
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.DateAndTimeCard
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.HeaderWithMenu
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.PrimaryBlue

/**
 * UI composable for displaying detailed information about a single gym class.
 *
 * @param classDetails The data to display, null if not yet loaded.
 * @param isLoading Whether the data is currently loading.
 * @param errorMessage Error message to display if something went wrong.
 * @param onOpenDrawer Callback triggered when the header menu button is pressed.
 */
@Composable
fun ClassDetailsPage(
    classDetails: GymClassDetailsResponse?,
    isLoading: Boolean,
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

            // If we have the gym class details data
            classDetails != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Header section with screen title
                    // and menu icon to open/close the navigation drawer menu
                    HeaderWithMenu(
                        title = "",
                        onMenuClick = onOpenDrawer
                    )

                    // Top section, displaying the name of the class and the instructor
                    ClassDetailsBanner(classDetails)

                    Spacer(modifier = Modifier.height(5.dp))

                    Column {
                        DateAndTimeCard(classDetails.startTime, classDetails.endTime)
                        Text("Club: ${classDetails.clubName}")
                        Text("Location: ${classDetails.locationName}")
                        Text("Description: ${classDetails.description}")
                    }
                }
            }
        }
    }
}
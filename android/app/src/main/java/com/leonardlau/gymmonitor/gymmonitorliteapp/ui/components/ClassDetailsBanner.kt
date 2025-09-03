package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import com.leonardlau.gymmonitor.gymmonitorliteapp.R
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.GymClassDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.PrimaryBlue


/**
 * A reusable component to be used at the top of any Gym Class Details page.
 * Displays the instructor's profile picture, their name, and the gym class name.
 *
 * @param classDetails Data object containing details of the gym class.
 */
@Composable
fun ClassDetailsBanner(
    classDetails: GymClassDetailsResponse
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .background(color = PrimaryBlue),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Instructor profile picture
            Image(
                // Profile pictures not yet supported by backend, just use a default
                painter = painterResource(id = R.drawable.default_profile_icon),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape), // Turn the image into a circle shape
                contentScale = ContentScale.Crop // Scale to fill container, crop overflow
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Instructor Name
            Text(
                text = classDetails.instructorName,
                fontSize = 20.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Name of Gym Class
            Text(
                text = classDetails.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
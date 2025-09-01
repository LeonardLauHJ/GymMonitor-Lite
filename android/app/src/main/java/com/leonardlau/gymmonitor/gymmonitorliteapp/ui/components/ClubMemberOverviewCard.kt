package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.R
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MemberOverview

/**
 * A reusable component which displays a summary of information about a club member.
 * Used in the Club Members overview page shown to staff users.
 *
 * @param memberOverview The [MemberOverview] containing the member's overview data.
 * @param fontSize The size of the text to display. Defaults to 17.sp
 */
@Composable
fun ClubMemberOverviewCard(
    memberOverview: MemberOverview,
    fontSize: androidx.compose.ui.unit.TextUnit = 17.sp,
) {
    // Card used for shaded outline
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 7.dp), // space outside the card
        elevation = CardDefaults.cardElevation(6.dp), // Shadow under the card
        shape = RectangleShape // no rounded corners (corners are rounded by default)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(color = Color(0xFFF9F9F9))
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Member user's profile picture
                Image(
                    // Profile pictures not yet supported by backend, just use a default
                    painter = painterResource(id = R.drawable.default_profile_icon),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape), // Turn the image into a circle shape
                    contentScale = ContentScale.Crop // Scale to fill container, crop overflow
                )

                // Spacing between the profile picture and the information
                Spacer(modifier = Modifier.width(15.dp))

                // Member user info
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = memberOverview.name)

                    // Divider line between the member's name and their other information
                    Spacer(modifier = Modifier.height(4.dp)) // little space above line
                    HorizontalDivider(thickness = 2.dp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp)) // little space below line

                    Text(text = "#${memberOverview.id}")
                    Text(text = memberOverview.membershipPlanName ?: "Membership Inactive")
                    Text(text = "Owes Us: ${memberOverview.owesUs}")
                }
            }
        }
    }

}

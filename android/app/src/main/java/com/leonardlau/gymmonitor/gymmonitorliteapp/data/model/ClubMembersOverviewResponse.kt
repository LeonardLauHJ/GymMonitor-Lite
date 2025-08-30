package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the JSON response we get from the backend /api/staff/members
 * endpoint, which is a list of detail summaries for each member in the staff user's club.
 */
data class ClubMembersOverviewResponse(
    val members: List<MemberOverview>
)
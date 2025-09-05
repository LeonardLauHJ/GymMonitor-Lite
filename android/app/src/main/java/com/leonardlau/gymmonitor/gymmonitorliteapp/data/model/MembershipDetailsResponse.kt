package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the JSON response we get from the membership details endpoint.
 * Contains information about the current user's active membership.
 */
data class MembershipDetailsResponse(
    val clubName: String,
    val dateJoined: String,
    val totalVisits: Int,
    val membershipPlanName: String,
    val nextBillingDate: String,
    val amountDue: String,
)

package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the overview of a single member's details, which gets shown to staff.
 */
data class MemberOverview(
    val id: Int,
    val name: String,
    val membershipPlanName: String?,
    val owesUs: String
)

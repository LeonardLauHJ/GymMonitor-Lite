package com.leonardlau.gymmonitor.gymmonitorlite.dto

/**
 * DTO for member detail summaries visible to staff members.
 *
 * @property id Unique identifier of the member.
 * @property name Full name of the member.
 * @property membershipPlanName Name of the member's current membership plan.
 * @property owesUs Formatted string of the amount the member owes the gym (e.g. "$15.00").
 */
data class StaffViewMemberSummaryDto(
    val id: Int,
    val name: String,
    val membershipPlanName: String?,
    val owesUs: String
)

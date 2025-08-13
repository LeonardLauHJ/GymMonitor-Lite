package com.leonardlau.gymmonitor.gymmonitorlite.dto

import java.time.Instant
import java.time.LocalDate

/**
 * Represents a member's current membership details.
 *
 * @property clubName Name of the club the user is a member of.
 * @property dateJoined Date the member joined the club.
 * @property totalVisits Total number of recorded visits.
 * @property membershipPlanName Name of the membership plan (null if not on a plan).
 * @property nextBillingDate Next date the membership will be billed (null if staff or no plan).
 * @property amountDue Amount owed to the club in dollars.
 */
data class MembershipDetailsDto(
    val clubName: String,
    val dateJoined: Instant,
    val totalVisits: Long,
    val membershipPlanName: String?,
    val nextBillingDate: LocalDate?,
    val amountDue: String
)
package com.leonardlau.gymmonitor.gymmonitorlite.dto

import com.leonardlau.gymmonitor.gymmonitorlite.dto.BookingSummaryDto

/**
 * DTO representing a user's dashboard.
 *
 * This DTO is returned from the dashboard endpoint and provides
 * a summary of the user's gym activity, bookings, and financials.
 *
 * @property dashboardTitle The title message of the dashboard
 * @property totalBookings The total number of bookings ever made by the user.
 * @property upcomingBookings A list of upcoming bookings represented in summary format.
 * @property totalVisits The total number of visits the user has made to the gym.
 * @property amountOwed The amount (in dollars) the user owes to the club, formatted as a string (e.g., "$12.34").
 */
data class DashboardDto(
    val dashboardTitle: String,
    val totalBookings: Int,
    val upcomingBookings: List<BookingSummaryDto>,
    val totalVisits: Long,
    val amountOwed: String
)
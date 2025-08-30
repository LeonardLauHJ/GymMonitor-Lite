package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the dashboard overview returned by the backend.
 */
data class DashboardResponse(
    val dashboardTitle: String,
    val totalBookings: Int,
    val upcomingBookings: List<BookingSummary>,
    val totalVisits: Int,
    val amountOwed: String
)
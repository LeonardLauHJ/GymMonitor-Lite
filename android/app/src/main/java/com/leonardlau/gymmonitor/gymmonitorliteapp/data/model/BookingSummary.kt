package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents a booking summary in the dashboard.
 */
data class BookingSummary(
    val className: String,
    val locationName: String,
    val startTime: String, // Will need to parse this into a readable format
    val durationMinutes: Long
)
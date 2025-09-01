package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents a gym class in the Member Timetable.
 */
data class TimetableEntry(
    val className: String,
    val instructorName: String,
    val locationName: String,
    val startTime: String, // Will need to parse this into a readable format
    val durationMinutes: Long,
    val currentBookings: Int,
    val maxCapacity: Int,
)
package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents a gym class in the Staff Schedule.
 */
data class StaffScheduleEntry(
    val classId: Int,
    val className: String,
    val locationName: String,
    val startTime: String, // Timestamp format, needs to be parsed
    val durationMinutes: Long,
    val currentBookings: Int,
    val maxCapacity: Int,
)
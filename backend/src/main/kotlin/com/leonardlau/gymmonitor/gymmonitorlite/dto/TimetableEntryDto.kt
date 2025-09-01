package com.leonardlau.gymmonitor.gymmonitorlite.dto

import java.time.LocalDateTime

/**
 * Represents a single gym class entry in the gym Class Timetable.
 * 
 * @property classId Id of the gym class.
 * @property className Name of the gym class.
 * @property instructorName Name of the staff member who is teaching the class
 * @property locationName Name of the location where the class is held.
 * @property startTime Starting time of the class.
 * @property durationMinutes Duration of the class in minutes.
 * @property currentBookings Number of current bookings for the class.
 * @property maxCapacity Maximum number of allowed participants.
 */
data class TimetableEntryDto(
    val classId: Int,
    val className: String,
    val instructorName: String,
    val locationName: String,
    val startTime: LocalDateTime,
    val durationMinutes: Long,
    val currentBookings: Int,
    val maxCapacity: Int
)
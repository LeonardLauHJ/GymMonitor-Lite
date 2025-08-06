package com.leonardlau.gymmonitor.gymmonitorlite.dto

import java.time.LocalDateTime
import java.time.Duration

/**
 * DTO for displaying limited info about a booking.
 *
 * @property className Name of the booked gym class.
 * @property locationName Location where the class is held.
 * @property startTime When the class starts.
 * @property durationMinutes Duration of the class in minutes.
 */
data class BookingSummaryDto (
    val className: String,
    val locationName: String,
    val startTime: LocalDateTime,
    val durationMinutes: Long
) {
    // Companion objects let us define functions callable on the class itself
    // eg another class can call BookingSummaryDto.fromBooking(it)
    companion object {
        /**
         * Builds a BookingSummaryDto from a Booking entity.
         * Extracts only class name, location, start time, and duration.
         */
        fun fromBooking(booking: com.leonardlau.gymmonitor.gymmonitorlite.entity.Booking): BookingSummaryDto  {
            val gymClass = booking.gymClass
            // Get the total duration by finding the length between the start and end time
            val duration = Duration.between(gymClass.startTime, gymClass.endTime).toMinutes()
            return BookingSummaryDto (
                className = gymClass.name,
                locationName = gymClass.location.name,
                startTime = gymClass.startTime,
                durationMinutes = duration
            )
        }
    }
}

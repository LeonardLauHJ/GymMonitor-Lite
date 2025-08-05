package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Booking
import org.springframework.data.jpa.repository.JpaRepository

interface BookingRepository : JpaRepository<Booking, Int> {
    /**
     * Returns the list of bookings made by the given user, with the given status, and starting after the given start time.
     */
    fun findByMemberAndStatusAndGymClass_StartTimeAfter(
        member: User, status: String, startTime: LocalDateTime
    ): List<Booking>
}
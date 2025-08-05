package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Booking
import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

/**
 * JPA Repository interface for managing Booking entities.
 */
interface BookingRepository : JpaRepository<Booking, Int> {
    /**
     * Finds all bookings made by the given member, with the given status,
     * where the associated gym class starts after the specified date and time.
     * 
     * @param member The member who made the bookings.
     * @param status The booking status to filter by (e.g., "BOOKED").
     * @param startTime The cutoff start time; only classes starting after this time are included.
     * @return A list of matching bookings.
     */
    fun findByMemberAndStatusAndGymClass_StartTimeAfter(
        member: User, status: String, startTime: LocalDateTime
    ): List<Booking>
}
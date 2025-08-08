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
     * Finds all bookings made by the given member.
     */
    fun findByMember(member: User): List<Booking>

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

    /**
     * Counts how many users have booked the given class.
     *
     * @param gymClass The class to check.
     * @return The number of current bookings for the class.
     */
    fun countByGymClass(gymClass: GymClass): Int


    /**
     * Counts how many bookings exist for a given gym class with a specific status.
     *
     * @param gymClassId The ID of the gym class.
     * @param status The status of the booking (e.g., "CONFIRMED", "CANCELLED").
     * @return The number of bookings that match the given class ID and status.
     */
    fun countByGymClassIdAndStatus(gymClassId: Int, status: String): Int

    /**
     * Counts how many classes the member has booked within the given time range.
     *
     * @param member The member to check.
     * @param start The start of the time range.
     * @param end The end of the time range.
     * @return The number of bookings within the time range.
     */
    fun countByMemberAndGymClass_StartTimeBetween(
        member: User,
        start: LocalDateTime,
        end: LocalDateTime
    ): Int

    /**
     * Checks if the given user has already booked the given class.
     *
     * @param member The user/member to check.
     * @param gymClass The class to check.
     * @return True if the member has already booked the class.
     */
    fun existsByMemberAndGymClass(member: User, gymClass: GymClass): Boolean

}
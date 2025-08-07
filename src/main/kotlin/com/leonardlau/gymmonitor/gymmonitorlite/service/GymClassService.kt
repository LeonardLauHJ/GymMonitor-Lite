package com.leonardlau.gymmonitor.gymmonitorlite.service

import com.leonardlau.gymmonitor.gymmonitorlite.entity.GymClass
import org.springframework.stereotype.Service
import com.leonardlau.gymmonitor.gymmonitorlite.repository.GymClassRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.BookingRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Service class for handling gym class-related logic.
 *
 * @property gymClassRepository JPA repository for GymClass entities.
 * @property bookingRepository JPA repository for Booking entities.
 */
@Service
class GymClassService(
    private val gymClassRepository: GymClassRepository,
    private val bookingRepository: BookingRepository
) {

    /**
     * Retrieves a gym class by its ID.
     *
     * @param id The ID of the gym class.
     * @return The GymClass entity, or null if not found.
     */
    fun getClassById(id: Int): GymClass? {
        return gymClassRepository.findById(id).orElse(null)
    }

    /**
     * Retrieves all gym classes for a specific club on a given date.
     *
     * @param clubId ID of the club.
     * @param date Date to retrieve classes for.
     * @return List of GymClass entities on that date.
     */
    fun getClassesForDate(clubId: Int, date: LocalDate): List<GymClass> {
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(LocalTime.MAX)
        return gymClassRepository.findByLocation_Club_IdAndStartTimeBetweenOrderByStartTimeAsc(
            clubId, startOfDay, endOfDay
        )
    }

    /**
     * Retrieves all upcoming gym classes for a specific club.
     *
     * @param clubId ID of the club.
     * @return List of upcoming GymClass entities for that club.
     */
    fun getAllUpcomingClassesForClub(clubId: Int): List<GymClass> {
        val now = LocalDateTime.now()
        return gymClassRepository.findByLocation_Club_IdAndStartTimeGreaterThanEqualOrderByStartTimeAsc(clubId, now)
    }

    /**
     * Returns the number of members currently booked into a specific class.
     *
     * @param gymClassId ID of the gym class.
     * @return Number of bookings with status "BOOKED".
     */
    fun getcurrentBookingsForClass(gymClassId: Int): Int {
        return bookingRepository.countByGymClassIdAndStatus(gymClassId, "BOOKED")
    }
}
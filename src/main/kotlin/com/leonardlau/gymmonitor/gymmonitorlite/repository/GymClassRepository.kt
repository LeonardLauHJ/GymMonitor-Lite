package com.leonardlau.gymmonitor.gymmonitorlite.repository

import java.time.LocalDateTime
import com.leonardlau.gymmonitor.gymmonitorlite.entity.GymClass
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA Repository interface for managing GymClass entities.
 */
interface GymClassRepository : JpaRepository<GymClass, Int> {
    /**
     * Find all gym classes scheduled at a specific club between the given start and end times.
     *
     * @param clubId The ID of the club to filter classes by.
     * @param start The start timestamp of the time range.
     * @param end The end timestamp of the time range.
     * @return A list of GymClass objects that start within the given time range, ordered by start time ascending.
     */
    fun findByLocation_Club_IdAndStartTimeBetweenOrderByStartTimeAsc(
        clubId: Int,
        start: LocalDateTime,
        end: LocalDateTime
    ): List<GymClass>
    
    /**
     * Find all upcoming classes scheduled at a specific club, ordered by their start time in ascending order.
     *
     * @param clubId The ID of the club to filter classes by.
     * @param startTime Only include classes starting at or after this time.
     * @return A list of GymClass objects for the given club, ordered by start time ascending.
     */
    fun findByLocation_Club_IdAndStartTimeGreaterThanEqualOrderByStartTimeAsc(
        clubId: Int,
        startTime: LocalDateTime
    ): List<GymClass>
}
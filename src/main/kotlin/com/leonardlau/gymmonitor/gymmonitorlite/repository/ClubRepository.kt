package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Club
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA Repository interface for managing Club entities.
 */
interface ClubRepository : JpaRepository<Club, Int> {
    /**
     * Finds the club by its unique code.
     *
     * @param code The unique code identifying the club.
     * @return The Club entity if found, or null if no club matches the code.
     */
    fun findByCode(code: String): Club?
}
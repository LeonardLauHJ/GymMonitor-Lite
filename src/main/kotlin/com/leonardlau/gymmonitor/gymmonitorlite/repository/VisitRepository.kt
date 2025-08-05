package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Visit
import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA Repository interface for managing Visit entities.
 */
interface VisitRepository : JpaRepository<Visit, Int> {
    /**
     * Counts the total number of visits made by the given member.
     *
     * @param member The member whose visits are being counted.
     * @return The count of visits.
     */
    fun countByMember(member: User): Long
}
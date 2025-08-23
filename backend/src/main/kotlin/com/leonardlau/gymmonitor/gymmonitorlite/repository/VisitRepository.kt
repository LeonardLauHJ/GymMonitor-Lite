package com.leonardlau.gymmonitor.gymmonitorlite.repository

import java.time.Instant
import com.leonardlau.gymmonitor.gymmonitorlite.entity.Visit
import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import com.leonardlau.gymmonitor.gymmonitorlite.entity.Club
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

    /**
     * Checks if a visit exists for the given member and club between the specified times.
     *
     * @param member The user/member.
     * @param club The club.
     * @param start Start of the time range (inclusive).
     * @param end End of the time range (exclusive).
     * @return True if a visit exists within the range, false otherwise.
     */
    fun existsByMemberAndClubAndScannedAtBetween(
        member: User,
        club: Club,
        start: Instant,
        end: Instant
    ): Boolean
}
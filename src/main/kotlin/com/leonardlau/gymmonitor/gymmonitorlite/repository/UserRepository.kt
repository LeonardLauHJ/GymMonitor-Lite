package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA Repository interface for managing User entities.
 */
interface UserRepository : JpaRepository<User, Int> {
    /**
     * Finds the user registered with the unique given email.
     *
     * @param email The email of the user to be found
     * @return The user registered with the given email, or null if no user has that email.
     */
    fun findByEmail(email: String): User?
}
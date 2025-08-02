package com.leonardlau.gymmonitor.gymmonitorlite.service

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import com.leonardlau.gymmonitor.gymmonitorlite.repository.UserRepository
import org.springframework.security.core.userdetails.User as SecurityUser

/**
 * Service for loading user details from the database.
 *
 * Used by Spring Security to authenticate users.
 *
 * @property userRepo Repository for querying users by email.
 */
@Service
class CustomUserDetailsService(
    private val userRepo: UserRepository
) : UserDetailsService {

    /**
     * Loads user details by email.
     *
     * @param email The email of the user to load.
     * @return UserDetails containing username, password, and roles.
     * @throws UsernameNotFoundException if no user is found with the given email.
     */
    override fun loadUserByUsername(email: String): UserDetails =
        // Find the user in the database with that email
		userRepo.findByEmail(email)?.let { user ->
			// Build a Spring Security User object using the found user's details
            org.springframework.security.core.userdetails.User
                .withUsername(user.email) // Set the username as the email
                .password(user.passwordHash)
                .roles(user.role) // Assigns the user's role as an authority (e.g. "MEMBER" becomes "ROLE_MEMBER")
                .build()
        } ?: throw UsernameNotFoundException("User not found") // Throw exception if user is not found
}

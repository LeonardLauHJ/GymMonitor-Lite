package com.leonardlau.gymmonitor.gymmonitorlite.controller

import com.leonardlau.gymmonitor.gymmonitorlite.dto.LoginDto
import com.leonardlau.gymmonitor.gymmonitorlite.dto.SignUpDto
import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import com.leonardlau.gymmonitor.gymmonitorlite.repository.ClubRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.MembershipPlanRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.UserRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.Instant

/**
 * Controller for authentication endpoints: signup and login.
 *
 * Handles registration for member user accounts, and logging in with JWT token authentication.
 * 
 * @property userRepository Repository for accessing and saving user data
 * @property clubRepository Repository for accessing club data
 * @property membershipPlanRepository Repository for accessing membership plan data
 * @property passwordEncoder Encodes passwords securely before saving to the database
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val clubRepository: ClubRepository,
    private val membershipPlanRepository: MembershipPlanRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    /**
     * Registers a new member user with the provided signup details.
     *
     * @param request SignUpDto containing name, email, password, clubCode, and membershipPlanId
     * @return HTTP 200 with success message or HTTP 400/409 with error message if signup fails
     */
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignUpDto): ResponseEntity<Any> {
        // Check if email is already in use by an existing account
        if (userRepository.findByEmail(request.email) != null) {
            return ResponseEntity.status(409).body(mapOf("error" to "Email is already in use"))
        }

        // Find club by clubCode
        val club = clubRepository.findByCode(request.clubCode)
            ?: return ResponseEntity.badRequest().body(mapOf("error" to "Invalid club code"))

        // Find membership plan by id
        val membershipPlan = membershipPlanRepository.findById(request.membershipPlanId).orElse(null)
            ?: return ResponseEntity.badRequest().body(mapOf("error" to "Membership plan not found"))

        // Ensure the membership plan belongs to the club being signed up to
        if (membershipPlan.club.id != club.id) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Membership plan does not belong to club"))
        }

        // Create a new user entity with the provided information
        val user = User(
            club = club,
            membershipPlan = membershipPlan,
            name = request.name,
            role = "MEMBER", // Only member accounts can be created with signup
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password), // Hash the password for security
            dateJoined = Instant.now(), // Set 
            balanceCents = 0
        )

        // Save the new user to the database
        userRepository.save(user)

        // Return a 200 OK with a message
        return ResponseEntity.ok(mapOf("message" to "User registered successfully"))
    }

    @PostMapping("/login")
    fun login(): ResponseEntity<String> {
        return ResponseEntity.ok("Login endpoint hit")
    }
}

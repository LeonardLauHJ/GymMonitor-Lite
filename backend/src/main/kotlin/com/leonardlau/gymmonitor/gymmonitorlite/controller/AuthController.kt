package com.leonardlau.gymmonitor.gymmonitorlite.controller

import com.leonardlau.gymmonitor.gymmonitorlite.dto.LoginDto
import com.leonardlau.gymmonitor.gymmonitorlite.dto.SignUpDto
import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import com.leonardlau.gymmonitor.gymmonitorlite.repository.ClubRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.MembershipPlanRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.UserRepository
import com.leonardlau.gymmonitor.gymmonitorlite.security.JwtUtil
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.LocalDate

/**
 * Controller for authentication endpoints: signup and login.
 *
 * Handles registration for member user accounts, and logging in with JWT token authentication.
 * 
 * @property userRepository Repository for accessing and saving user data
 * @property clubRepository Repository for accessing club data
 * @property membershipPlanRepository Repository for accessing membership plan data
 * @property passwordEncoder Encodes passwords securely before saving to the database
 * @property authenticationManager Authenticates login credentials
 * @property jwtUtil Utility for generating and validating JWT tokens
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val clubRepository: ClubRepository,
    private val membershipPlanRepository: MembershipPlanRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
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

        // Calculate the initial next billing date based on the membership plan's billing period.
        // If the user has no membership plan (e.g., staff), this will be null.
        val initialBillingDate: LocalDate? = membershipPlan?.let { // var?.let will only run the body if var is not null
            // Take the current date and add the number of days from the chosen plan's billing period
            LocalDate.now().plusDays(it.billingPeriodDays.toLong()) // 'it' refers to the membershipPlan instance
        }

        // Create a new user entity with the provided information
        val user = User(
            club = club,
            membershipPlan = membershipPlan,
            name = request.name,
            role = "MEMBER", // Only member accounts can be created with signup
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password), // Hash the password for security
            dateJoined = Instant.now(),
            centsOwed = 0,
            nextBillingDate = initialBillingDate
        )

        // Save the new user to the database
        userRepository.save(user)

        // Return a 200 OK with a message
        return ResponseEntity.ok(mapOf("message" to "User registered successfully"))
    }

    /**
     * Authenticates user with email and password, returning JWT token if successful.
     *
     * @param request LoginDto containing email and password
     * @return HTTP 200 with JWT token or HTTP 401 if authentication fails
     */
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginDto): ResponseEntity<Any> {
        return try {
            // Try to authenticate the user using the provided email and password
            val auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )

            // Get the user details from the authentication result
            val userDetails = auth.principal as org.springframework.security.core.userdetails.User

            // Get the user with the matching email (saved in userDetails as username) from the database
            val user = userRepository.findByEmail(userDetails.username)
                ?: return ResponseEntity.status(401).body(mapOf("error" to "User not found"))

            // Generate JWT token for authenticated user
            val token = jwtUtil.generateToken(user)

            // If authentication succeeded and user is found, return a 200 OK with the JWT token in the response body.
            // This token will be used to authenticate the user in future requests
            ResponseEntity.ok(mapOf("token" to token))

        } catch (ex: org.springframework.security.core.AuthenticationException) {
            // Authentication failed due to bad credentials
            ResponseEntity.status(401).body(mapOf("error" to "Invalid email or password"))
        }
    }

    /**
     * Checks if the current user is authenticated.
     *
     * @param userDetails the authenticated user details.
     * @return 200 OK with user id, name, and role if authenticated;
     *         401 Unauthorized with a message if not authenticated.
     */
    @GetMapping("/check")
    fun checkAuth(@AuthenticationPrincipal userDetails: CustomUserDetails?): ResponseEntity<Any> {
        return if (userDetails != null) {
            ResponseEntity.ok(
                mapOf(
                    "id" to userDetails.id,
                    "name" to userDetails.name,
                    "role" to userDetails.role
                )
            )
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("message" to "Unauthorized"))
        }
    }

}

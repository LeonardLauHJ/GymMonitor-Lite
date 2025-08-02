package com.leonardlau.gymmonitor.gymmonitorlite.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*
import com.leonardlau.gymmonitor.gymmonitorlite.entity.User

/**
 * Utility class for generating and validating JWT tokens.
 *
 * @property jwtSecret Secret key used to sign the token.
 * @property jwtExpirationMs Token expiration time in milliseconds.
 */
@Component
class JwtUtil(
    // Values should be stored in .env and injected
    @Value("\${app.jwtSecret}") private val jwtSecret: String,
    @Value("\${app.jwtExpirationMs}") private val jwtExpirationMs: Long
) {
    // Convert the secret string into a key used to sign and verify JWTs
    private val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user to generate the token for.
     * @return The JWT token string.
     */
    fun generateToken(user: User): String {
		// Get current time
        val now = Instant.now()
        return Jwts.builder()
            .setSubject(user.email) // Set token subject as user email (unique identifier)
			.claim("roles", user.role) // Add the user role ("member" or "staff") as a custom claim
			.setIssuedAt(Date.from(now)) // Set the current time as when the token was issued
			.setExpiration(Date.from(now.plusMillis(jwtExpirationMs))) // Set token expiry time
			.signWith(key, SignatureAlgorithm.HS256) // Sign the token with secret key using HS256 algorithm
			.compact() // Build and serialize the token into a compact JWT string
    }

    /**
     * Extracts the email (subject) from a token.
     *
     * @param token The JWT token.
     * @return Email (subject) or null if invalid.
     */
    fun extractEmail(token: String): String? =
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject

    /**
     * Validates the JWT token's signature and expiration.
     *
     * @param token The JWT token.
     * @return True if valid, false otherwise.
     */
    fun validateToken(token: String): Boolean {
        return try {
            // Try parsing the token with the signing key
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            // If no exceptions, then the token is valid, return true
            true
        } catch (ex: JwtException) {
            // If there was an exception (invalid token/signature or expired), return false
            false
        }
    }
}

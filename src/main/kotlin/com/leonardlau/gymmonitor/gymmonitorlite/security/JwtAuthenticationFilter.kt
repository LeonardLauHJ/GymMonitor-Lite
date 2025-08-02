package com.leonardlau.gymmonitor.gymmonitorlite.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import com.leonardlau.gymmonitor.gymmonitorlite.service.CustomUserDetailsService
import org.springframework.security.core.userdetails.UserDetails

/**
 * Filter that intercepts incoming requests to validate JWT tokens and set the authentication context.
 *
 * @property jwtUtil Utility for validating and parsing JWT tokens.
 * @property userDetailsService Service for loading user details by email.
 */
@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    /**
     * Filters incoming requests to authenticate users based on their JWT token.
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
		// Get the "Authorization" header from the request (Should be in the format "Bearer <token>")
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
		// Extract the token part after "Bearer "
        val token = authHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

		// If token exists and is valid
        if (token != null && jwtUtil.validateToken(token)) {
            // Extract the user’s email (subject) from the token
            val email = jwtUtil.extractEmail(token)
            
            // If email was found and no authentication is currently set in the security context...
            if (email != null && SecurityContextHolder.getContext().authentication == null) {
                // Load full user details from database/service using the email
                val userDetails = userDetailsService.loadUserByUsername(email)

                // Create an authentication token including user details and granted authorities (roles)
                val auth = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                
                // Attach request details (eg IP address, session ID) to the authentication token
                auth.details = WebAuthenticationDetailsSource().buildDetails(request)

                // Set the authentication in the security context, marking the user as logged in
                SecurityContextHolder.getContext().authentication = auth
            }
        }

		// Continue with the rest of the filter chain (allow the request to proceed)
        filterChain.doFilter(request, response)
    }
}

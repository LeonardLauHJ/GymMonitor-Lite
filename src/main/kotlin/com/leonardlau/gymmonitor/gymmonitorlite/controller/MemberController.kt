package com.leonardlau.gymmonitor.gymmonitorlite.controller

import com.leonardlau.gymmonitor.gymmonitorlite.dto.BookingSummaryDto
import com.leonardlau.gymmonitor.gymmonitorlite.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for endpoints accessible by authenticated members.
 * Displays dashboard data including bookings, visits, and payment info.
 * 
 * @property userService Service for retrieving user-related data.
 */
@RestController
@RequestMapping("/api/member")
class MemberController(
    private val userService: UserService
) {

    /**
     * Returns a dashboard overview for the authenticated member.
     * 
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @return A response entity with dashboard information including bookings, visits, and payment due.
     */
    @GetMapping("/dashboard")
    fun viewDashboard(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Any> {
        val user = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "User not found"))

        // Convert full Booking entities into BookingSummaryDto format
        // (Only shows gym class name, location, start time, duration)
        val upcomingBookings = userService.getUpcomingBookings(user)
            .map { BookingSummaryDto.fromBooking(it) }

        val totalVisits = userService.getTotalVisits(user)
        val centsOwed = userService.getCentsOwed(user)

        val response = mapOf(
            "message" to "${user.name}'s Dashboard",
            "Total Bookings" to upcomingBookings.size,
            "Upcoming Bookings" to upcomingBookings,
            "Total Visits" to totalVisits,
            "Amount owed to the club" to "$%.2f".format(centsOwed / 100.0) // Formats cents to dollar amount (e.g., 1234 -> $12.34)
        )

        return ResponseEntity.ok(response)
    }
}

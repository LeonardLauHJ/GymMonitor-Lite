package com.leonardlau.gymmonitor.gymmonitorlite.controller

import java.time.LocalDate
import java.time.format.DateTimeParseException
import com.leonardlau.gymmonitor.gymmonitorlite.dto.BookingSummaryDto
import com.leonardlau.gymmonitor.gymmonitorlite.dto.DashboardDto
import com.leonardlau.gymmonitor.gymmonitorlite.dto.TimetableEntryDto
import com.leonardlau.gymmonitor.gymmonitorlite.dto.MembershipDetailsDto
import com.leonardlau.gymmonitor.gymmonitorlite.service.UserService
import com.leonardlau.gymmonitor.gymmonitorlite.service.GymClassService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for endpoints accessible by authenticated member users.
 * 
 * @property userService Service for retrieving user-related data.
 * @property gymClassService Service for retrieving gym-class-related data.
 */
@RestController
@RequestMapping("/api/member")
class MemberController(
    private val userService: UserService,
    private val gymClassService: GymClassService
) {

    /**
     * Returns a dashboard overview for the authenticated member.
     * 
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @return A response entity with dashboard information including bookings, visits, and payment due.
     */
    @GetMapping("/dashboard")
    fun viewDashboard(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Any> {
        // Get the currently authenticated user, return a 404 error if not found
        val user = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "User not found"))

        // Convert full Booking entities into BookingSummaryDto format
        // (Only shows gym class name, location, start time, duration)
        val upcomingBookings = userService.getUpcomingBookings(user)
            .map { BookingSummaryDto.fromBooking(it) }

        val totalVisits = userService.getTotalVisits(user)
        val centsOwed = userService.getCentsOwed(user)

        // Pack the dashboard data into a strongly typed DTO, for a clear, consistent, and easily extendable API response.
        val dashboardDetails = DashboardDto(
            dashboardTitle = "${user.name}'s Dashboard",
            totalBookings = upcomingBookings.size,
            upcomingBookings = upcomingBookings,
            totalVisits = totalVisits,
            amountOwed = "$%.2f".format(centsOwed / 100.0)
        )

        return ResponseEntity.ok(dashboardDetails)
    }

    /**
     * Returns the timetable of gym classes happening at the authenticated member's club.
     *
     * If a date is provided in the query (/timetable?date=YYYY-MM-DD), only classes on that date will be returned.
     * Otherwise, all upcoming classes at the club will be shown.
     *
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @param date (Optional) The date to filter classes by. Format: YYYY-MM-DD.
     * @return A list of timetable entries showing class name, instructor name, location, start time, duration, bookings, and capacity.
     */
    @GetMapping("/timetable")
    fun viewClubTimetable(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam(required = false) date: String? = null // Date is optional
    ): ResponseEntity<Any> {
        // Get the currently authenticated user, return a 404 error if not found
        val user = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "User not found"))

        // Get the club which the user belongs to
        val clubId = user.club.id
        
        // If a date was given as a parameter, get a list of all gym classes at that club on the given date
        val gymClasses = if (date != null) {
            // Attempt to parse the given date (should be in format YYYY-MM-DD) into a LocalDate object.
            val parsedDate = try {
                LocalDate.parse(date)
            } catch (e: DateTimeParseException) {
                // If the given date can't be parsed (invalid format), return an error
                return ResponseEntity.badRequest().body(mapOf("error" to "Invalid date format. Use YYYY-MM-DD"))
            }
            gymClassService.getClassesForDate(clubId, parsedDate)
        } else {
            // If no date was given, get a list of all upcoming classes at the club
            gymClassService.getAllUpcomingClassesForClub(clubId)
        }

        // Convert the list of found gym classes into display format (TimetableEntryDto)
        val timetableEntries = gymClasses.map { gymClass ->
            val currentBookings = gymClassService.getcurrentBookingsForClass(gymClass.id)
            val durationMinutes = java.time.Duration.between(gymClass.startTime, gymClass.endTime).toMinutes()
            TimetableEntryDto(
                className = gymClass.name,
                instructorName = gymClass.staff.name,
                locationName = gymClass.location.name,
                startTime = gymClass.startTime,
                durationMinutes = durationMinutes,
                currentBookings = currentBookings,
                maxCapacity = gymClass.maxCapacity
            )
        }

        return ResponseEntity.ok(timetableEntries)
    }

    /**
     * Returns membership details for the authenticated member.
     *
     * This includes the club name, the date the member joined, total visits made,
     * the name of the active membership plan (if any), the next billing date, and the
     * total amount currently owed to the club.
     *
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @return A response entity containing the member's membership details, or a 404 error if the user cannot be found.
     */
    @GetMapping("/membership")
    fun viewMembership(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Any> {
        // Get the currently authenticated user, return a 404 error if not found
        val user = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "User not found"))

        // Get the user's number of total visits
        val totalVisits = userService.getTotalVisits(user)

        val membershipDetails = MembershipDetailsDto(
            clubName = user.club.name,
            dateJoined = user.dateJoined,
            totalVisits = totalVisits,
            membershipPlanName = user.membershipPlan?.name,
            nextBillingDate = user.nextBillingDate,
            amountDue = "$%.2f".format(userService.getCentsOwed(user) / 100.0) // Formats cents to dollar amount (e.g., 1234 -> $12.34)
        )

        return ResponseEntity.ok(membershipDetails)
    }

    /**
     * Records a visit for the authenticated member to their club.
     * Simulates the member's "scan in" at the club.
     * 
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @return A response entity containing a success response and the member's total number of visits, or 404 if user is not found.
     */
    @PostMapping("/scan-in")
    fun scanIn(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Any> {
        // Get the currently authenticated user, return a 404 error if not found
        val user = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "User not found"))

        // Create a new Visit at the current time and save it to the database
        userService.recordVisit(user)

        return ResponseEntity.ok(mapOf("message" to "Scan successful", "totalVisits" to userService.getTotalVisits(user)))
    }

}

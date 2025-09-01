package com.leonardlau.gymmonitor.gymmonitorlite.controller

import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.net.URI
import com.leonardlau.gymmonitor.gymmonitorlite.dto.StaffViewMemberSummaryDto
import com.leonardlau.gymmonitor.gymmonitorlite.dto.StaffScheduleEntryDto
import com.leonardlau.gymmonitor.gymmonitorlite.dto.CreateGymClassRequestDto
import com.leonardlau.gymmonitor.gymmonitorlite.dto.GymClassDetailsDto
import com.leonardlau.gymmonitor.gymmonitorlite.service.UserService
import com.leonardlau.gymmonitor.gymmonitorlite.service.GymClassService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for endpoints accessible by authenticated staff users.
 */
@RestController
@RequestMapping("/api/staff")
class StaffController(
    private val userService: UserService,
    private val gymClassService: GymClassService
) {

    /**
     * Retrieve a list of all members belonging to the same club as the currently authenticated staff user, with relevant information.
     *
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @return A list of members in the staff user's club, with their id, name, membership plan name, and the amount they owe.
     */
    @GetMapping("/members")
    fun viewClubMembers(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Any> {
        // Get the currently authenticated staff user, return a 404 error if not found
        val staffUser = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "Staff user not found"))

        // Get the club which the staff user belongs to
        val clubId = staffUser.club.id

        // Get a list of all users who are members of the club
        val clubMembers = userService.getMembersByClubId(clubId)

        // For each member from the list, get their details and package them into a nice summary
        val members = clubMembers.map { member ->
            StaffViewMemberSummaryDto(
                id = member.id,
                name = member.name,
                membershipPlanName = member.membershipPlan?.name,
                owesUs = "$%.2f".format(userService.getCentsOwed(member) / 100.0) // Formats cents to dollar amount (e.g., 1234 -> $12.34)
            )
        }

        return ResponseEntity.ok(members)
    }

    /**
     * Returns the schedule of gym classes which the authenticated staff user is teaching.
     *
     * If a date is provided in the query (/timetable?date=YYYY-MM-DD), only classes on that date will be returned.
     * Otherwise, all upcoming classes at the club will be shown.
     *
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @param date (Optional) The date to filter classes by. Format: YYYY-MM-DD.
     * @return A list of scheduled class entries showing class name, location, start time, duration, bookings, and capacity.
     */
    @GetMapping("/schedule")
    fun viewSchedule(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam(required = false) date: String? = null // Date is optional
    ): ResponseEntity<Any> {
        // Get the currently authenticated staff user, return a 404 error if not found
        val staffUser = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "Staff user not found"))

        // If a date was given as a parameter, parse it into a LocalDate object
        val gymClasses = if (date != null) {
            // Attempt to parse the given date (should be in format YYYY-MM-DD) into a LocalDate object.
            val parsedDate = try {
                LocalDate.parse(date)
            } catch (e: DateTimeParseException) {
                // If the given date can't be parsed (invalid format), return an error
                return ResponseEntity.badRequest().body(mapOf("error" to "Invalid date format. Use YYYY-MM-DD"))
            }
            gymClassService.getClassesStaffTeachesOnDate(staffUser.id, parsedDate)
        } else {
            // Otherwise, get all upcoming classes for this staff member
            gymClassService.getAllUpcomingClassesStaffTeaches(staffUser.id)
        }

        val scheduleEntries = gymClasses.map { gymClass ->
            val currentBookings = gymClassService.getcurrentBookingsForClass(gymClass.id)
            val durationMinutes = java.time.Duration.between(gymClass.startTime, gymClass.endTime).toMinutes()
            StaffScheduleEntryDto(
                classId = gymClass.id,
                className = gymClass.name,
                locationName = gymClass.location.name,
                startTime = gymClass.startTime,
                durationMinutes = durationMinutes,
                currentBookings = currentBookings,
                maxCapacity = gymClass.maxCapacity
            )
        }

        return ResponseEntity.ok(scheduleEntries)
    }

    /**
     * Creates a new gym class for the authenticated staff member.
     *
     * This endpoint allows a staff user to create a new gym class at one of the
     * locations in their club. The staff member is automatically assigned as the
     * instructor of the class.
     *
     * Example request JSON:
     * {
     *   "locationId": 2,
     *   "name": "Evening Yoga",
     *   "description": "Relaxing flow yoga session",
     *   "startTime": "2025-08-20T18:00:00",
     *   "endTime": "2025-08-20T19:00:00",
     *   "maxCapacity": 20
     * }
     *
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @param request The request body containing the new class details, following the format of CreateGymClassRequestDto.
     * @return The created gym class' details, or an error message if creation failed.
     */
    @PostMapping("/classes")
    fun createClass(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody request: CreateGymClassRequestDto // Request body, must follow the structure of CreateGymClassRequestDto.
    ): ResponseEntity<Any> {
        // NOTE: Currently no restrictions on classes you can create e.g. scheduling conflicts

        // Get the currently authenticated staff user, return a 404 error if not found
        val staffUser = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "Staff user not found"))

        // Attempt to create the gym class, save it to the database and respond with the created gym class entity
        return try {
            val gymClass = gymClassService.createClass(staffUser, request)

            // Convert to GymClassDetailsDto format
            val gymClassDetails = GymClassDetailsDto(
                id = gymClass.id,
                name = gymClass.name,
                instructorName = gymClass.staff.name,
                startTime = gymClass.startTime,
                endTime = gymClass.endTime,
                clubName = gymClass.location.club.name,
                locationName = gymClass.location.name,
                description = gymClass.description
            )

            ResponseEntity.created(URI.create("/api/classes/${gymClass.id}")) // Location of the new class' details page
                          .body(gymClassDetails) // Response body with the created gym class' details

        } catch (ex: IllegalArgumentException) {
            // If an error occured with creating the class (failed validation), respond with the error message
            ResponseEntity.badRequest().body(mapOf("error" to ex.message))
        }
    }

}
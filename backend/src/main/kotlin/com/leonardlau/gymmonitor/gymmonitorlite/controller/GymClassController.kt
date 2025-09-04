package com.leonardlau.gymmonitor.gymmonitorlite.controller

import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import com.leonardlau.gymmonitor.gymmonitorlite.entity.GymClass
import com.leonardlau.gymmonitor.gymmonitorlite.dto.GymClassDetailsDto
import com.leonardlau.gymmonitor.gymmonitorlite.service.GymClassService
import com.leonardlau.gymmonitor.gymmonitorlite.service.UserService
import com.leonardlau.gymmonitor.gymmonitorlite.service.BookingResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails

/**
 * Controller for gym class-related endpoints.
 *
 * @property gymClassService Service for managing gym class logic.
 */
@RestController
@RequestMapping("/api/classes")
class GymClassController(
    private val gymClassService: GymClassService,
    private val userService: UserService
) {

    /**
     * Returns detailed information about a gym class.
     * This includes the class name, instructor name, date, time range, club, location, description
     *
     * @param id The ID of the gym class to retrieve.
     * @return A response entity containing class details, or 404 if not found.
     */
    @GetMapping("/{id}")
    fun getClassDetails(
        @PathVariable id: Int,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Any> {
        // Get the currently authenticated user, if any.
        val user = userService.findByEmail(userDetails.username)

        // Attempt to get the gym class with the given id. If it doesn't exist, return a 404 error
        val gymClass = gymClassService.getClassById(id)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "Class not found"))

        // Get the booking status for the user. Either "CAN_BOOK" if the user is able to book the class,
        // otherwise "CANNOT_BOOK"
        val bookingStatus = gymClassService.getBookingStatusForMember(user, gymClass)

        val gymClassDetails = GymClassDetailsDto(
            id = gymClass.id,
            name = gymClass.name,
            instructorName = gymClass.staff.name,
            startTime = gymClass.startTime,
            endTime = gymClass.endTime,
            clubName = gymClass.location.club.name,
            locationName = gymClass.location.name,
            description = gymClass.description,
            bookingStatus = bookingStatus
        )

        return ResponseEntity.ok(gymClassDetails)
    }

    /**
     * Books a class for the authenticated member.
     *
     * @param id The ID of the gym class to book.
     * @param userDetails The authenticated user details.
     * @return A success message, or an error message with appropriate status codes:
     * - 404 if user or class not found
     * - 403 if the user is not a member
     * - 400 if already booked, class is full, or weekly booking limit reached
     */
    @PostMapping("/{id}/book")
    fun bookClass(
        @PathVariable id: Int,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Any> {
        // Get the currently authenticated user, return an error 404 if not found
        val user = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "User not found"))

        // Check that the user is a member (staff cannot book classes)
        if (user.role != "MEMBER") {
            return ResponseEntity.status(403).body(mapOf("error" to "Only members can book classes"))
        }

        // Attempt to book the class
        val result = gymClassService.bookClass(id, user)

        // What we return depends on the result of the booking attempt
        return when (result) {
            is BookingResult.Success -> ResponseEntity.status(201).body((mapOf("message" to "Successfully booked class")))
            is BookingResult.ClassInPast -> ResponseEntity.badRequest().body(mapOf("error" to "Cannot book a class in the past"))
            is BookingResult.AlreadyBooked -> ResponseEntity.badRequest().body(mapOf("error" to "You have already booked this class"))
            is BookingResult.Full -> ResponseEntity.badRequest().body(mapOf("error" to "This class is full"))
            is BookingResult.NotFound -> ResponseEntity.status(404).body(mapOf("error" to "Class not found"))
            is BookingResult.WeeklyBookingLimitReached -> ResponseEntity.badRequest().body(mapOf("error" to "Weekly booking limit has already been reached"))
            else -> ResponseEntity.internalServerError().body(mapOf("error" to "Unknown error"))
        }
    }

}

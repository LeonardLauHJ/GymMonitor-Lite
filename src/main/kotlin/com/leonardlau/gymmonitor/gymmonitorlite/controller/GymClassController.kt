package com.leonardlau.gymmonitor.gymmonitorlite.controller

import com.leonardlau.gymmonitor.gymmonitorlite.dto.GymClassDetailsDto
import com.leonardlau.gymmonitor.gymmonitorlite.service.GymClassService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for gym class-related endpoints.
 *
 * @property gymClassService Service for managing gym class logic.
 */
@RestController
@RequestMapping("/api/classes")
class GymClassController(
    private val gymClassService: GymClassService
) {

    /**
     * Returns detailed information about a gym class.
     * This includes the class name, instructor name, date, time range, club, location, description
     *
     * @param id The ID of the gym class to retrieve.
     * @return A response entity containing class details, or 404 if not found.
     */
    @GetMapping("/{id}")
    fun getClassDetails(@PathVariable id: Int): ResponseEntity<Any> {
        // Attempt to get the gym class with the given id. If it doesn't exist, return a 404 error
        val gymClass = gymClassService.getClassById(id)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "Class not found"))

        val gymClassDetails = GymClassDetailsDto(
            name = gymClass.name,
            instructorName = gymClass.staff.name,
            startTime = gymClass.startTime,
            endTime = gymClass.endTime,
            clubName = gymClass.location.club.name,
            locationName = gymClass.location.name,
            description = gymClass.description
        )

        return ResponseEntity.ok(gymClassDetails)
    }
}

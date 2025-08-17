package com.leonardlau.gymmonitor.gymmonitorlite.dto

import java.time.LocalDateTime

/**
 * The Request body structure that should be used for creating a new gym class.
 * Includes all fields of the GymClass entity except for id (auto incremented), 
 * and staff id (will automatically assign the id of the staff member who created it)
 *
 * @property locationId The ID of the location where the class will be held.
 * @property name The name/title of the gym class.
 * @property description A short description of the class.
 * @property startTime The start time of the class (ISO-8601 format).
 * @property endTime The end time of the class (ISO-8601 format).
 * @property maxCapacity The maximum number of attendees allowed.
 */
data class CreateGymClassRequestDto(
    val locationId: Int,
    val name: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val maxCapacity: Int
)

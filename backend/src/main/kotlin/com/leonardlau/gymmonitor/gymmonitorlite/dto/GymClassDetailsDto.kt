package com.leonardlau.gymmonitor.gymmonitorlite.dto

import java.time.LocalDateTime

/**
 * DTO representing detailed info about a gym class for the gym class details page.
 * 
 * @property id Id of the gym class.
 * @property name Name of the gym class.
 * @property instructorName Location where the class is held.
 * @property startTime When the class starts.
 * @property endTime When the class ends.
 * @property clubName Name of the club this class belongs to.
 * @property locationName Location where the class is held.
 * @property description Description of the gym class.
 */
data class GymClassDetailsDto(
    val id: Int,
    val name: String,
    val instructorName: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val clubName: String,
    val locationName: String,
    val description: String
)

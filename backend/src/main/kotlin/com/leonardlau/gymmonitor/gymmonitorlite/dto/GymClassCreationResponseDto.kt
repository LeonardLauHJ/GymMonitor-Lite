package com.leonardlau.gymmonitor.gymmonitorlite.dto

import java.time.LocalDateTime

/**
 * DTO representing the format of data to return after creating a Gym Class. 
 * Contains detailed info about a gym class for the gym class details page.
 * (Same as GymClassDetailsDto except for the bookingStatus property)
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
data class GymClassCreationResponseDto(
    val id: Int,
    val name: String,
    val instructorName: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val clubName: String,
    val locationName: String,
    val description: String
)

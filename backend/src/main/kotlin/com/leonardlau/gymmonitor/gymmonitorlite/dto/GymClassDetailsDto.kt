package com.leonardlau.gymmonitor.gymmonitorlite.dto

import java.time.LocalDateTime

/**
 * DTO representing detailed info about a gym class for the gym class details page.
 */
data class GymClassDetailsDto(
    val name: String,
    val instructorName: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val clubName: String,
    val locationName: String,
    val description: String
)

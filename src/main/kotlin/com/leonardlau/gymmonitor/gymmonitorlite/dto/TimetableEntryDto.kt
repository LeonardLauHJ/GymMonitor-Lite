package com.leonardlau.gymmonitor.gymmonitorlite.dto

import java.time.LocalDateTime

data class TimetableEntryDto(
    val className: String,
    val locationName: String,
    val startTime: LocalDateTime,
    val durationMinutes: Long,
    val currentCapacity: Int,
    val maxCapacity: Int
)
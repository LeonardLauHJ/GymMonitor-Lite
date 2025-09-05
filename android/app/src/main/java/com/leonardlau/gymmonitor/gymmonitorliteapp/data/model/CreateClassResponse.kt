package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the JSON response from the backend after successfully
 * creating a new gym class.
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
data class CreateClassResponse (
    val id: Int,
    val name: String,
    val instructorName: String,
    val startTime: String,
    val endTime: String,
    val clubName: String,
    val locationName: String,
    val description: String
)
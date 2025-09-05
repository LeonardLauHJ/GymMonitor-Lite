package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the JSON body to send when a staff user creates a new gym class.
 *
 * @property locationId Id of the gym class.
 * @property name Name of the gym class.
 * @property description Description of the gym class.
 * @property startTime When the class starts.
 * @property endTime When the class ends.
 * @property maxCapacity The maximum number of members who can book this class.
 */
data class CreateClassRequest(
    val locationId: Int,
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val maxCapacity: Int
)
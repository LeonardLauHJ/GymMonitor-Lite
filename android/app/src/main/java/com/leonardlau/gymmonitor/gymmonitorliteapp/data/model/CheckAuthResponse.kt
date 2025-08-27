package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the JSON response we get after making a GET request
 * to the /auth/check endpoint.
 */
data class CheckAuthResponse(
    val id: Int,
    val name: String,
    val role: String
)
package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the JSON body to send when a user logs in.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
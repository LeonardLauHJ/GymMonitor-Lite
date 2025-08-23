package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents the JSON body to send when a user signs up.
 */
data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val clubCode: String,
    val membershipPlanId: Int
)
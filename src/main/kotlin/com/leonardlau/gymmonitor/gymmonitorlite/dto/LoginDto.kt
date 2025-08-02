package com.leonardlau.gymmonitor.gymmonitorlite.dto

/**
 * Data Transfer Object (DTO) for user login requests.
 *
 * Specifies the format of data needed when logging in,
 * contains all information required to authenticate a user.
 *
 * @property email User's email address.
 * @property password User's password.
 */
data class LoginDto(
    val email: String,
    val password: String
)

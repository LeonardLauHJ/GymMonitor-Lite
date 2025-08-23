package com.leonardlau.gymmonitor.gymmonitorlite.dto

/**
 * Data Transfer Object (DTO) for user signup requests.
 *
 * Specifies the format of data needed when signing up,
 * contains all information required to create a new user.
 *
 * @property name User's full name.
 * @property email User's email address.
 * @property password User's chosen password.
 * @property clubCode Code identifying the user's club.
 * @property membershipPlanId ID of the membership plan selected by the user.
 */
data class SignUpDto(
    val name: String,
    val email: String,
    val password: String,
    val clubCode: String,
    val membershipPlanId: Int
)

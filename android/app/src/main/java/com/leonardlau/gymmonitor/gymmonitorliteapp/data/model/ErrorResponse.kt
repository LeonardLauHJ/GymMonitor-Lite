package com.leonardlau.gymmonitor.gymmonitorliteapp.data.model

/**
 * Data class that represents an error response from the backend,
 * which should be a simple 'error' field with a message.
 */
data class ErrorResponse(
    val error: String
)
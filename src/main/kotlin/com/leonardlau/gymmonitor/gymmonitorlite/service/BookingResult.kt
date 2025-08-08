package com.leonardlau.gymmonitor.gymmonitorlite.service

/**
 * Represents the possible results of a booking attempt.
 */
sealed class BookingResult {
    // This is a sealed class, which lists all the possible outcomes of a function (in this case, BookingResult()).
    // Only one of these results is returned at a time, and Kotlin needs all cases to be handled.

    /** Booking was successful. */
    object Success : BookingResult()

    /** The user has already booked this class. */
    object AlreadyBooked : BookingResult()

    /** The class is already full. */
    object Full : BookingResult()

    /** The class with the given ID was not found. */
    object NotFound : BookingResult()

    /** The user has already reached their weekly booking limit. */
    object WeeklyBookingLimitReached : BookingResult()
}

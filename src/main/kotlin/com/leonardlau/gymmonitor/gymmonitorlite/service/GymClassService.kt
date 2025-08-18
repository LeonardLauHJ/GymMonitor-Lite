package com.leonardlau.gymmonitor.gymmonitorlite.service

import com.leonardlau.gymmonitor.gymmonitorlite.dto.CreateGymClassRequestDto
import com.leonardlau.gymmonitor.gymmonitorlite.entity.GymClass
import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import com.leonardlau.gymmonitor.gymmonitorlite.entity.Booking
import org.springframework.stereotype.Service
import com.leonardlau.gymmonitor.gymmonitorlite.repository.GymClassRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.LocationRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.BookingRepository
import com.leonardlau.gymmonitor.gymmonitorlite.service.BookingResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Service class for handling gym class-related logic.
 *
 * @property gymClassRepository JPA repository for GymClass entities.
 * @property bookingRepository JPA repository for Booking entities.
 */
@Service
class GymClassService(
    private val gymClassRepository: GymClassRepository,
    private val bookingRepository: BookingRepository,
    private val locationRepository: LocationRepository
) {

    /**
     * Retrieves a gym class by its ID.
     *
     * @param id The ID of the gym class.
     * @return The GymClass entity, or null if not found.
     */
    fun getClassById(id: Int): GymClass? {
        return gymClassRepository.findById(id).orElse(null)
    }

    /**
     * Retrieves all gym classes for a specific club on a given date.
     *
     * @param clubId ID of the club.
     * @param date Date to retrieve classes for.
     * @return List of GymClass entities on that date.
     */
    fun getClassesForDate(clubId: Int, date: LocalDate): List<GymClass> {
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(LocalTime.MAX)
        return gymClassRepository.findByLocation_Club_IdAndStartTimeBetweenOrderByStartTimeAsc(
            clubId, startOfDay, endOfDay
        )
    }

    /**
     * Retrieves all upcoming gym classes for a specific club.
     *
     * @param clubId ID of the club.
     * @return List of upcoming GymClass entities for that club.
     */
    fun getAllUpcomingClassesForClub(clubId: Int): List<GymClass> {
        val now = LocalDateTime.now()
        return gymClassRepository.findByLocation_Club_IdAndStartTimeGreaterThanEqualOrderByStartTimeAsc(clubId, now)
    }

    /**
     * Returns the number of members currently booked into a specific class.
     *
     * @param gymClassId ID of the gym class.
     * @return Number of bookings with status "BOOKED".
     */
    fun getcurrentBookingsForClass(gymClassId: Int): Int {
        return bookingRepository.countByGymClassIdAndStatus(gymClassId, "BOOKED")
    }

    /**
     * Attempts to book a class for the given user.
     *
     * Booking is only allowed if:
     * - The class exists
     * - The class has not yet started
     * - The user hasn't already booked the class
     * - The class has not reached its max capacity
     * - The user's membership plan allows more bookings this week
     *
     * @param classId ID of the gym class to book.
     * @param user The member attempting to book.
     * @return A BookingResult representing the outcome of the booking attempt.
     */
    fun bookClass(classId: Int, user: User): BookingResult {
        // Get the gym class being booked, return the not found result if it cannot be found
        val gymClass = gymClassRepository.findById(classId).orElse(null) ?: return BookingResult.NotFound

        // Check that the class start time has not already passed
        if (gymClass.startTime.isBefore(LocalDateTime.now())) {
            return BookingResult.ClassInPast
        }

        // Check if user has already booked this class
        if (bookingRepository.existsByMemberAndGymClass(user, gymClass)) {
            return BookingResult.AlreadyBooked
        }

        // Check if class is full
        val currentBookings = bookingRepository.countByGymClass(gymClass)
        if (currentBookings >= gymClass.maxCapacity) {
            return BookingResult.Full
        }

        // Check that this booking does not exceed the member's number of allowed
        // classes per week
        val plan = user.membershipPlan
        if (plan != null) {
            // To do this, we need to figure out what week the class is happening on,
            // then check the number of classes the user has booked for that week, 
            // and make sure that they are not already at their limit
            
            // Find the Monday (start) of the week for the class’s scheduled start date
            val classWeekStart = gymClass.startTime
                .toLocalDate()
                .with(java.time.DayOfWeek.MONDAY) // Shift to Monday of that week
                .atStartOfDay() // Set time to 00:00

            // Calculate the end of that week, 7 days later
            val classWeekEnd = classWeekStart.plusDays(7)

            // Count how many classes the user has booked that start within the same week
            val bookingsInThatWeek = bookingRepository.countByMemberAndGymClass_StartTimeBetween(
                member = user,
                start = classWeekStart,
                end = classWeekEnd
            )

            // If the user has already reached their weekly class limit, block the booking
            if (bookingsInThatWeek >= plan.classesPerWeek) {
                return BookingResult.WeeklyBookingLimitReached
            }
        }

        // If all checks have passed
        // Save the booking to the database and return a success result
        val booking = Booking(
            gymClass = gymClass,
            member = user,
            status = "BOOKED"
        )
        bookingRepository.save(booking)
        return BookingResult.Success
    }

    /**
     * Retrieves all gym classes taught by a specific staff user on a given date.
     *
     * @param staffId ID of the instructor teaching the classes.
     * @param date Date to retrieve classes for.
     * @return List of GymClass entities taught by that instructor on the given date.
     */
    fun getClassesStaffTeachesOnDate(staffId: Int, date: LocalDate): List<GymClass> {
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(LocalTime.MAX)
        return gymClassRepository.findByStaff_IdAndStartTimeBetweenOrderByStartTimeAsc(
            staffId, startOfDay, endOfDay
        )
    }

    /**
     * Retrieves all upcoming gym classes taught by a specific staff user.
     *
     * @param staffId ID of the instructor teaching the classes.
     * @return List of upcoming GymClass entities taught by that instructor.
     */
    fun getAllUpcomingClassesStaffTeaches(staffId: Int): List<GymClass> {
        val now = LocalDateTime.now()
        return gymClassRepository.findByStaff_IdAndStartTimeGreaterThanEqualOrderByStartTimeAsc(
            staffId, now
        )
    }

    /**
     * Creates a new gym class for the given staff user. The staff user will automatically be assigned as the instructor.
     *
     * @param staff The staff user creating the class.
     * @param request The details of the new class.
     * @return The created GymClass entity.
     * @throws IllegalArgumentException if the location is invalid or does not belong to the staff's club.
     */
    fun createClass(staff: User, request: CreateGymClassRequestDto): GymClass {
        // Check that the given location exists
        val location = locationRepository.findById(request.locationId)
            .orElseThrow { IllegalArgumentException("Location not found") }

        // Ensure the given location belongs to the same club as the staff
        if (location.club.id != staff.club.id) {
            throw IllegalArgumentException("Location does not belong to staff's club")
        }

        // Ensure that the end time is after the start time
        if (!request.endTime.isAfter(request.startTime)) {
            throw IllegalArgumentException("End time must be after start time")
        }

        // If the checks pass, create the gym class and save it to the database
        val gymClass = GymClass(
            location = location,
            staff = staff,
            name = request.name,
            description = request.description,
            startTime = request.startTime,
            endTime = request.endTime,
            maxCapacity = request.maxCapacity
        )
        return gymClassRepository.save(gymClass)
    }

}
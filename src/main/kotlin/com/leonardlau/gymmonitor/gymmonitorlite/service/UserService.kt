package com.leonardlau.gymmonitor.gymmonitorlite.service

import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import com.leonardlau.gymmonitor.gymmonitorlite.repository.BookingRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.UserRepository
import com.leonardlau.gymmonitor.gymmonitorlite.repository.VisitRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Service class for user-related business logic.
 *
 * @property userRepository Repository for User entities.
 * @property bookingRepository Repository for Booking entities.
 * @property visitRepository Repository for Visit entities.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val bookingRepository: BookingRepository,
    private val visitRepository: VisitRepository
) {

    /**
     * Finds a User entity by their email address.
     *
     * @param email Email of the user.
     * @return The User entity or null if not found.
     */
    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    /**
     * Retrieves all bookings made by the user.
     * 
     * @param user The user to retrieve bookings for.
     * @return List of the user's bookings.
     */
    fun getAllBookings(user: User) = bookingRepository.findByMember(user)

    /**
     * Retrieves the upcoming bookings for a user.
     * Only bookings with status "BOOKED" and gym class start time after now are returned.
     *
     * @param user The user to retrieve bookings for.
     * @return List of upcoming bookings.
     */
    fun getUpcomingBookings(user: User) =
        bookingRepository.findByMemberAndStatusAndGymClass_StartTimeAfter(user, "BOOKED", LocalDateTime.now())

    /**
     * Counts the total visits a user has made to any club.
     *
     * @param user The user to count visits for.
     * @return The number of visits.
     */
    fun getTotalVisits(user: User): Long {
        return visitRepository.countByMember(user)
    }

    /**
     * Retrieves the membership plan assigned to the user.
     *
     * @param user The user to retrieve the membership plan for.
     * @return The membership plan or null if none assigned.
     */
    fun getMembershipPlan(user: User) = user.membershipPlan

    /**
     * Retrieves the current number of cents owed by the user to their club.
     *
     * @param user The user to retrieve the amount for.
     * @return The number of cents owed by the user to their club.
     */
    fun getCentsOwed(user: User) = user.centsOwed

    /**
     * Charges all members whose `nextBillingDate` is today or earlier.
     *
     * This method should be called daily by a scheduler.
     * For each user with a non-null `nextBillingDate` that is due:
     * - Adds their membership plan's cost to their `centsOwed`
     * - Updates their `nextBillingDate` by adding the billing period
     */
    fun chargeDueMembers() {
        // Filter users with a billing date that is due or past
        val membersToCharge = userRepository.findAll().filter { user ->
            user.nextBillingDate != null &&
            user.membershipPlan != null &&
            !(user.nextBillingDate!!.isAfter(LocalDate.now())) // the !! asserts that nextBillingDate is not null (safe as we've already checked)
        }

        // Go through each member that needs to be charged
        for (user in membersToCharge) {
            val plan = user.membershipPlan!! // Safe to assert not null as all members should have a membershipPlan
            val billingAmount = plan.priceCents
            val billingPeriodDays = plan.billingPeriodDays

            // Add the plan's cost to the amount the user owes
            user.centsOwed += billingAmount

            // Schedule the next billing date by adding the plan's number of days between payments to the current date
            user.nextBillingDate = user.nextBillingDate!!.plusDays(billingPeriodDays.toLong())

            // Save the updated user
            userRepository.save(user)
        }
    }

    /**
     * Records a visit for the given member to their club, at the current time.
     *
     * @param member The member who is scanning in to their club.
     */
    fun recordVisit(member: User) {
        val visit = Visit(
            member = member,
            club = member.club
            // the scannedAt column automatically assigns the current time
        )
        visitRepository.save(visit)
    }

}

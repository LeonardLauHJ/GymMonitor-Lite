package com.leonardlau.gymmonitor.gymmonitorlite.scheduler

import com.leonardlau.gymmonitor.gymmonitorlite.service.UserService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * A scheduled component that handles daily billing logic.
 *
 * This class runs once per day (at 2:00 AM server time) and checks if any member accounts
 * have reached their next billing date. If so, it adds the appropriate charge to their balance
 * and schedules their next billing date.
 *
 * @property userService Used to apply billing logic to all relevant users.
 */
@Component
class BillingScheduler(
    private val userService: UserService
) {

    /**
     * Scheduled method that runs daily at 2:00 AM.
     *
     * In real-world systems, off-peak times (like 2 AM) are typically chosen to minimise
     * load and avoid interfering with user activity.
     */
    @Scheduled(cron = "0 0 2 * * *") // cron = "second minute hour day_of_month month day_of_week"
    fun runDailyBilling() {
        userService.chargeDueMembers()
    }
}
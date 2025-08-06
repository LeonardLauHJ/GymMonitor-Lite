package com.leonardlau.gymmonitor.gymmonitorlite.controller

import com.leonardlau.gymmonitor.gymmonitorlite.service.UserService

/**
 * Controller for endpoints accessible by authenticated members.
 * Displays dashboard data including bookings, visits, and payment info.
 * 
 * @property userService Service for retrieving user-related data.
 */
@RestController
@RequestMapping("/api/member")
class MemberController(
    private val userService: UserService
) {

}

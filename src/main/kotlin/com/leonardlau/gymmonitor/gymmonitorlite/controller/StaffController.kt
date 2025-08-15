package com.leonardlau.gymmonitor.gymmonitorlite.controller

import com.leonardlau.gymmonitor.gymmonitorlite.dto.StaffViewMemberSummaryDto
import com.leonardlau.gymmonitor.gymmonitorlite.service.UserService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.http.ResponseEntity

/**
 * Controller for endpoints accessible by authenticated staff users.
 */
@RestController
@RequestMapping("/api/staff")
class StaffController(
    private val userService: UserService
) {

    /**
     * Retrieve a list of all members belonging to the same club as the currently authenticated staff user, with relevant information.
     *
     * @param userDetails Spring Security object containing the authenticated user's details (from JWT).
     * @return A list of members in the staff user's club, with their id, name, membership plan name, and the amount they owe.
     */
    @GetMapping("/members")
    fun viewClubMembers(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Any> {
         // Get the currently authenticated staff user, return a 404 error if not found
        val staffUser = userService.findByEmail(userDetails.username)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "Staff user not found"))

        val clubId = staffUser.club.id

        // Get a list of all users who are members of the club
        val clubMembers = userService.getMembersByClubId(clubId)

        // For each member from the list, get their details and package them into a nice summary
        val members = clubMembers.map { member ->
            StaffViewMemberSummaryDto(
                id = member.id,
                name = member.name,
                membershipPlanName = member.membershipPlan?.name,
                owesUs = "$%.2f".format(userService.getCentsOwed(member) / 100.0) // Formats cents to dollar amount (e.g., 1234 -> $12.34)
            )
        }

        return ResponseEntity.ok(members)
    }
}
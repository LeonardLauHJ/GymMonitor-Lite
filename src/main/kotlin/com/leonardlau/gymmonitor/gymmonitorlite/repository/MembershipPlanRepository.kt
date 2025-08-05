package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.MembershipPlan
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA Repository interface for managing MembershipPlan entities.
 */
interface MembershipPlanRepository : JpaRepository<MembershipPlan, Int>
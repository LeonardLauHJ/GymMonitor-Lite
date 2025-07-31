package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.MembershipPlan
import org.springframework.data.repository.JpaRepository

interface MembershipPlanRepository : JpaRepository<MembershipPlan, Int>
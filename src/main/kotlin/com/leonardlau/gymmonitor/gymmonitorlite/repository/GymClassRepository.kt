package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.GymClass
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA Repository interface for managing GymClass entities.
 */
interface GymClassRepository : JpaRepository<GymClass, Int>
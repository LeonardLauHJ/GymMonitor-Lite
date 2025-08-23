package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Location
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA Repository interface for managing Location entities.
 */
interface LocationRepository : JpaRepository<Location, Int>
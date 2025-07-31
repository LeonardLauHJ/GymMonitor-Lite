package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Location
import org.springframework.data.repository.JpaRepository

interface LocationRepository : JpaRepository<Location, Int>
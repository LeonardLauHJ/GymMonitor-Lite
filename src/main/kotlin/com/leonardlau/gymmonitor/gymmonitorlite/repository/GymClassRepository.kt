package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.GymClass
import org.springframework.data.repository.JpaRepository

interface GymClassRepository : JpaRepository<GymClass, Int>
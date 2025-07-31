package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Visit
import org.springframework.data.jpa.repository.JpaRepository

interface VisitRepository : JpaRepository<Visit, Int>
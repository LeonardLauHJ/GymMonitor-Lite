package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Club
import org.springframework.data.jpa.repository.JpaRepository

interface ClubRepository : JpaRepository<Club, Int>
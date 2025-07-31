package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.Booking
import org.springframework.data.repository.JpaRepository

interface BookingRepository : JpaRepository<Booking, Int>
package com.leonardlau.gymmonitor.gymmonitorlite.repository

import com.leonardlau.gymmonitor.gymmonitorlite.entity.User
import org.springframework.data.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int>
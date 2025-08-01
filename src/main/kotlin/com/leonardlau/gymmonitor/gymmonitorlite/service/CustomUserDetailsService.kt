package com.leonardlau.gymmonitor.gymmonitorlite.service

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import com.leonardlau.gymmonitor.gymmonitorlite.repository.UserRepository
import org.springframework.security.core.userdetails.User as SecurityUser

@Service
class CustomUserDetailsService(
  private val userRepo: UserRepository
): UserDetailsService {
  override fun loadUserByUsername(email: String): UserDetails =
    userRepo.findByEmail(email)?.let { user ->
      org.springframework.security.core.userdetails.User
        .withUsername(user.email)
        .password(user.passwordHash)
        .roles(user.role)
        .build()
    } ?: throw UsernameNotFoundException("User not found")
}

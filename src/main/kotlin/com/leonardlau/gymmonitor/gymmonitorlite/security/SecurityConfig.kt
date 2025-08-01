package com.leonardlau.gymmonitor.gymmonitorlite.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import com.leonardlau.gymmonitor.gymmonitorlite.security.JwtAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
  private val jwtFilter: JwtAuthenticationFilter
) {

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
    authConfig.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity, jwtFilter: JwtAuthenticationFilter): SecurityFilterChain {
    http.csrf().disable()
        .authorizeHttpRequests { auth ->
        auth.requestMatchers("/auth/**").permitAll()
            .anyRequest().authenticated()
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
    return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}
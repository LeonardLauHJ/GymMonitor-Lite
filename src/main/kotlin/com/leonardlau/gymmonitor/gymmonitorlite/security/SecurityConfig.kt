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

/**
 * Spring Security configuration class for the application.
 *
 * Enables JWT-based stateless authentication with method-level security.
 *
 * @property jwtFilter Filter that processes JWT tokens for authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtFilter: JwtAuthenticationFilter
) {

    /**
     * Provides the authentication manager bean used by Spring Security for authenticating users.
     */
    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager

    /**
     * Configures the security filter chain
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/auth/**").permitAll() // Allow unauthenticated access to auth endpoints (e.g. /login, /signup)
                    .anyRequest().authenticated() // Authentication required for any other requests
            }
            .sessionManagement { 
                // Tell Spring Security not to create or use HTTP sessions (as JWT tokens will be used instead)
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
            }
            // Add the custom JWT filter before the built-in username/password filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    /**
     * Configures the password encoder bean for hashing with BCrypt.
     */
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

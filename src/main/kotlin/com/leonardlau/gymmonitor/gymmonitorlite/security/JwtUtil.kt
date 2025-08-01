package com.leonardlau.gymmonitor.gymmonitorlite.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*
import com.leonardlau.gymmonitor.gymmonitorlite.entity.User

@Component
class JwtUtil(
  @Value("\${app.jwtSecret}") private val jwtSecret: String,
  @Value("\${app.jwtExpirationMs}") private val jwtExpirationMs: Long
) {
  private val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))

  fun generateToken(user: User): String {
    val now = Instant.now()
    return Jwts.builder()
      .setSubject(user.email)
      .claim("roles", listOf(user.role))
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plusMillis(jwtExpirationMs)))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact()
  }

  fun extractEmail(token: String): String? =
    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject

  fun validateToken(token: String): Boolean {
    return try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
      true
    } catch (ex: JwtException) {
      false
    }
  }
}

package com.leonardlau.gymmonitor.gymmonitorlite.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @PostMapping("/signup")
    fun signup(): ResponseEntity<String> {
        return ResponseEntity.ok("Signup endpoint hit")
    }

    @PostMapping("/login")
    fun login(): ResponseEntity<String> {
        return ResponseEntity.ok("Login endpoint hit")
    }
}

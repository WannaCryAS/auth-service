package com.wannacry.auth_service.controller

import com.wannacry.auth_service.model.request.LoginRequest
import com.wannacry.auth_service.model.request.RegisterRequest
import com.wannacry.auth_service.model.request.PasswordRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.repository.UserRepository
import com.wannacry.auth_service.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }

    @PostMapping("/password/first-time-set")
    fun setPassword(@RequestBody request: PasswordRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.setFirstTime(request))
    }

    @PostMapping("/password/reset")
    fun resetPassword(@RequestBody request: PasswordRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.resetPassword(request))
    }

}
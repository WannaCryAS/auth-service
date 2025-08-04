package com.wannacry.auth_service.controller

import com.wannacry.auth_service.model.request.AuthRequest
import com.wannacry.auth_service.model.request.LoginRequest
import com.wannacry.auth_service.model.request.PasswordRequest
import com.wannacry.auth_service.model.request.RegisterRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.processor.LoginProcessor
import com.wannacry.auth_service.processor.RegisterProcessor
import com.wannacry.auth_service.processor.ResetProcessor
import com.wannacry.auth_service.processor.SetFirstTimeProcessor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val registerProcessor: RegisterProcessor,
    private val loginProcessor: LoginProcessor,
    private val setFirstTimeProcessor: SetFirstTimeProcessor,
    private val resetPassword: ResetProcessor
) {
    @PostMapping("/register")
    fun register(@RequestBody request: AuthRequest<RegisterRequest>): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(registerProcessor.process(request.data))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest<LoginRequest>): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(loginProcessor.process(request.data))
    }

    @PostMapping("/password/first-time-set")
    fun setPassword(@RequestBody request: AuthRequest<PasswordRequest>): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(setFirstTimeProcessor.process(request.data))
    }

    @PostMapping("/password/reset")
    fun resetPassword(@RequestBody request: AuthRequest<PasswordRequest>): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(resetPassword.process(request.data))
    }

}
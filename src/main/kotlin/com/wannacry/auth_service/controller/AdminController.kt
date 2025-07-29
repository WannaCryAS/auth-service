package com.wannacry.auth_service.controller

import com.wannacry.auth_service.model.request.RegisterRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

class AdminController(
    private val authService: AuthService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/register")
    fun registerUserByAdmin(@RequestBody request: List<RegisterRequest>): ResponseEntity<AuthResponse> {
        val newUser = authService.registerByAdmin(request)
        return ResponseEntity.ok(newUser)
    }
}
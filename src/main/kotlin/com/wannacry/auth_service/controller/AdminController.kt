package com.wannacry.auth_service.controller

import com.wannacry.auth_service.model.request.AuthRequest
import com.wannacry.auth_service.model.request.RegisterRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.processor.AdminRegistrationProcessor
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

class AdminController(
    private val adminRegistrationProcessor: AdminRegistrationProcessor
) {

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/register")
    fun registerUserByAdmin(@RequestBody request: AuthRequest<List<RegisterRequest>>): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(adminRegistrationProcessor.process(requestList = request.data))
    }
}
package com.wannacry.auth_service.processor

import com.wannacry.auth_service.model.User
import com.wannacry.auth_service.model.request.RegisterRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.service.AuthService
import com.wannacry.auth_service.util.ResponseCode.SUCCESS
import com.wannacry.auth_service.util.ResponseCode.USERNAME_ALREADY_EXIST
import org.springframework.stereotype.Component

@Component
class RegisterProcessor(
    private val authService: AuthService,
) {
    fun process(request: RegisterRequest): AuthResponse {
        if (authService.isUsernameTaken(request.userName)) {
            return AuthResponse(
                responseCode = USERNAME_ALREADY_EXIST.first,
                description = USERNAME_ALREADY_EXIST.second
            )
        }

        val password = if (request.password.isNullOrBlank() && request.role == "TEACHER") {
            null
        } else {
            authService.encodePassword(request.password.orEmpty())
        }

        val firstTimeLogin = password.isNullOrBlank()

        val user = User(
            fullName = request.fullName,
            userName = request.userName,
            email = request.email,
            userPassword = password,
            phoneNumber = request.phoneNumber,
            role = request.role,
            firstTimeLogin = firstTimeLogin
        )
        authService.saveUser(user)

        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }
}
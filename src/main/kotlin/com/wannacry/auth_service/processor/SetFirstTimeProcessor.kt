package com.wannacry.auth_service.processor

import com.wannacry.auth_service.model.request.PasswordRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.service.AuthService
import com.wannacry.auth_service.util.ResponseCode.PASSWORD_ALREADY_SET_FIRST_SETUP
import com.wannacry.auth_service.util.ResponseCode.SUCCESS
import com.wannacry.auth_service.util.ResponseCode.USER_NOT_FOUND
import org.springframework.stereotype.Component

@Component
class SetFirstTimeProcessor(
    private val authService: AuthService
) {

    fun process(request: PasswordRequest): AuthResponse {
        val user = authService.findUserByUsername(request.userName)
            ?: return AuthResponse(
                responseCode = USER_NOT_FOUND.first,
                description = USER_NOT_FOUND.second
            )

        if (authService.isFirstTimeLogin(user).not()) {
            return AuthResponse(
                responseCode = PASSWORD_ALREADY_SET_FIRST_SETUP.first,
                description = PASSWORD_ALREADY_SET_FIRST_SETUP.second
            )
        }

        user.userPassword = authService.encodePassword(request.newPassword)
        user.firstTimeLogin = false
        authService.saveUser(user)

        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }
}
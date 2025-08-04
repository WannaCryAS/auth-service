package com.wannacry.auth_service.processor

import com.wannacry.auth_service.model.request.PasswordRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.service.AuthService
import com.wannacry.auth_service.util.ResponseCode.PASSWORD_NOT_SET
import com.wannacry.auth_service.util.ResponseCode.PASSWORD_SAME
import com.wannacry.auth_service.util.ResponseCode.SUCCESS
import com.wannacry.auth_service.util.ResponseCode.USER_NOT_FOUND
import org.springframework.stereotype.Component

@Component
class ResetProcessor(
    private val authService: AuthService
) {
    fun process(request: PasswordRequest): AuthResponse {
        val user = authService.findUserByUsername(request.userName)
            ?: return AuthResponse(
                responseCode = USER_NOT_FOUND.first,
                description = USER_NOT_FOUND.second
            )

        if (authService.isFirstTimeLogin(user)) {
            return AuthResponse(
                responseCode = PASSWORD_NOT_SET.first,
                description = PASSWORD_NOT_SET.second
            )
        }

        if (authService.isPasswordMatch(request.newPassword, user.userPassword!!)) {
            return AuthResponse(
                responseCode = PASSWORD_SAME.first,
                description = PASSWORD_SAME.second
            )
        }

        user.userPassword = authService.encodePassword(request.newPassword)
        authService.saveUser(user)

        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }
}
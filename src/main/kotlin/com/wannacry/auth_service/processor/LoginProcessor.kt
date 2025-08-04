package com.wannacry.auth_service.processor

import com.wannacry.auth_service.model.Token
import com.wannacry.auth_service.model.request.LoginRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.service.AuthService
import com.wannacry.auth_service.service.JwtService
import com.wannacry.auth_service.util.ResponseCode.FIRST_TIME_LOGIN
import com.wannacry.auth_service.util.ResponseCode.LOGIN_PASSWORD_INCORRECT
import com.wannacry.auth_service.util.ResponseCode.SUCCESS
import com.wannacry.auth_service.util.ResponseCode.USER_NOT_FOUND_LOGIN
import org.springframework.stereotype.Component

@Component
class LoginProcessor(
    private val authService: AuthService,
    private val jwtService: JwtService
) {
    fun process(request: LoginRequest): AuthResponse {
        val user = authService.findUserByUsername(request.userName)

        if (user?.userName.isNullOrBlank())
            return AuthResponse(
                responseCode = USER_NOT_FOUND_LOGIN.first,
                description = USER_NOT_FOUND_LOGIN.second
            )

        if (authService.isFirstTimeLogin(user!!))
            return AuthResponse(
                responseCode = FIRST_TIME_LOGIN.first,
                description = FIRST_TIME_LOGIN.second
            )

        if (!authService.isPasswordMatch(request.password, user.password!!))
            return AuthResponse(
                responseCode = LOGIN_PASSWORD_INCORRECT.first,
                description = LOGIN_PASSWORD_INCORRECT.second
            )

        val accessToken = jwtService.generateToken(user)
        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second,
            responseData = Token(
                accessToken = accessToken,
                refreshToken = "refresh_token" //Todo will create refreshToken later
            )
        )
    }
}
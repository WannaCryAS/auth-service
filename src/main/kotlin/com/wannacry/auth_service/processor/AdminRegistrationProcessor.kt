package com.wannacry.auth_service.processor

import com.wannacry.auth_service.model.User
import com.wannacry.auth_service.model.request.RegisterRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.service.AuthService
import com.wannacry.auth_service.util.ResponseCode.REGISTER_BY_ADMIN_ALREADY_EXIST
import com.wannacry.auth_service.util.ResponseCode.SUCCESS
import org.springframework.stereotype.Component

@Component
class AdminRegistrationProcessor(
    private val authService: AuthService
) {
    fun process(requestList: List<RegisterRequest>): AuthResponse {
        val createUser = mutableListOf<User>()

        requestList.forEach { request ->
            val user = authService.findUserByUsername(request.userName)
            if (authService.isUsernameTaken(request.userName))
                return AuthResponse(
                    responseCode = REGISTER_BY_ADMIN_ALREADY_EXIST.first,
                    description = REGISTER_BY_ADMIN_ALREADY_EXIST.second,
                    responseData = user?.userName
                )

            val password = if (request.password.isNullOrBlank() && request.role == "TEACHER") {
                null
            } else {
                authService.encodePassword(request.password.orEmpty())
            }

            val eachUser = User(
                fullName = request.fullName,
                userName = request.userName,
                email = request.email,
                userPassword = password,
                phoneNumber = request.phoneNumber,
                role = request.role
            )

            createUser.add(eachUser)
        }

        authService.saveMultipleUser(createUser)

        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }
}
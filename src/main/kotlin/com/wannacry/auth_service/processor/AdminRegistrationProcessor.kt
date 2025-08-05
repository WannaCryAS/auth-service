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
        val existingUsernames = getExistingUserName(requestList)
        if (existingUsernames.isNotEmpty()) {
            return existingUserResponse(existingUsernames)
        }

        val newUser = userList(requestList)
        authService.saveMultipleUser(newUser)

        return successResponse()
    }

    private fun getExistingUserName(requestList: List<RegisterRequest>): List<String> {
        return requestList
            .filter {
                authService.isUsernameTaken(it.userName)
            }
            .map { it.userName }
    }

    private fun userList(requestList: List<RegisterRequest>): List<User> {
        return requestList.map { request ->
            val password = if (request.password.isNullOrBlank() && request.role == "TEACHER") {
                null
            } else {
                authService.encodePassword(request.password.orEmpty())
            }

            User(
                fullName = request.fullName,
                userName = request.userName,
                email = request.email,
                userPassword = password,
                phoneNumber = request.phoneNumber,
                role = request.role
            )
        }
    }

    private fun existingUserResponse(usernames: List<String>): AuthResponse {
        return AuthResponse(
            responseCode = REGISTER_BY_ADMIN_ALREADY_EXIST.first,
            description = REGISTER_BY_ADMIN_ALREADY_EXIST.second,
            responseData = usernames
        )
    }

    private fun successResponse(): AuthResponse {
        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }
}
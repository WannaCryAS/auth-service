package com.wannacry.auth_service.service

import com.wannacry.auth_service.model.User
import com.wannacry.auth_service.model.request.RegisterRequest
import com.wannacry.auth_service.model.response.AuthResponse
import com.wannacry.auth_service.repository.UserRepository
import com.wannacry.auth_service.model.Token
import com.wannacry.auth_service.model.request.LoginRequest
import com.wannacry.auth_service.model.request.PasswordRequest
import com.wannacry.auth_service.util.ResponseCode.FIRST_TIME_LOGIN
import com.wannacry.auth_service.util.ResponseCode.LOGIN_PASSWORD_INCORRECT
import com.wannacry.auth_service.util.ResponseCode.PASSWORD_ALREADY_SET_FIRST_SETUP
import com.wannacry.auth_service.util.ResponseCode.PASSWORD_NOT_SET
import com.wannacry.auth_service.util.ResponseCode.PASSWORD_SAME
import com.wannacry.auth_service.util.ResponseCode.REGISTER_BY_ADMIN_ALREADY_EXIST
import com.wannacry.auth_service.util.ResponseCode.SUCCESS
import com.wannacry.auth_service.util.ResponseCode.USERNAME_ALREADY_EXIST
import com.wannacry.auth_service.util.ResponseCode.USER_NOT_FOUND
import com.wannacry.auth_service.util.ResponseCode.USER_NOT_FOUND_LOGIN
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.findByUserName(request.userName) != null)
            return AuthResponse(
                responseCode = USERNAME_ALREADY_EXIST.first,
                description = USERNAME_ALREADY_EXIST.second
            )


        val password = if (request.password.isNullOrBlank() && request.role == "TEACHER") {
            null
        } else {
            passwordEncoder.encode(request.password)
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
        userRepository.save(user)

        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByUserName(request.userName)
            ?: return AuthResponse(
                responseCode = USER_NOT_FOUND_LOGIN.first,
                description = USER_NOT_FOUND_LOGIN.second
            )

        if (user.userPassword.isNullOrBlank() || user.firstTimeLogin) {
            return AuthResponse(
                responseCode = FIRST_TIME_LOGIN.first,
                description = FIRST_TIME_LOGIN.second
            )
        }

        if (!passwordEncoder.matches(request.password, user.userPassword)) {
            return AuthResponse(
                responseCode = LOGIN_PASSWORD_INCORRECT.first,
                description = LOGIN_PASSWORD_INCORRECT.second
            )
        }

        val accessToken = jwtService.generateToken(user)
        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second,
            data = Token(
                accessToken = accessToken,
                refreshToken = "refresh_token" //Todo will create refreshToken later
            )
        )
    }

    fun registerByAdmin(requestList: List<RegisterRequest>): AuthResponse {
        val createUser = mutableListOf<User>()
        requestList.forEach { request ->
            if (userRepository.findByUserName(request.userName) != null)
                return AuthResponse(
                    responseCode = REGISTER_BY_ADMIN_ALREADY_EXIST.first,
                    description = REGISTER_BY_ADMIN_ALREADY_EXIST.second
                )

            val password = if (request.password.isNullOrBlank() && request.role == "TEACHER") {
                ""
            } else {
                passwordEncoder.encode(request.password)
            }

            val user = User(
                fullName = request.fullName,
                userName = request.userName,
                email = request.email,
                userPassword = password,
                phoneNumber = request.phoneNumber,
                role = request.role
            )

            createUser.add(user)
        }

        userRepository.saveAll(createUser)

        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }

    fun setFirstTime(request: PasswordRequest): AuthResponse {
        val user = userRepository.findByUserName(request.userName)
            ?: return AuthResponse(
                responseCode = USER_NOT_FOUND.first,
                description = USER_NOT_FOUND.second
            )

        if (!user.firstTimeLogin) {
            return AuthResponse(
                responseCode = PASSWORD_ALREADY_SET_FIRST_SETUP.first,
                description = PASSWORD_ALREADY_SET_FIRST_SETUP.second
            )
        }

        user.userPassword = passwordEncoder.encode(request.newPassword)
        user.firstTimeLogin = false
        userRepository.save(user)

        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }

    fun resetPassword(request: PasswordRequest): AuthResponse {
        val user = userRepository.findByUserName(request.userName)
            ?: return AuthResponse(
                responseCode = USER_NOT_FOUND.first,
                description = USER_NOT_FOUND.second
            )

        if (user.firstTimeLogin) {
            return AuthResponse(
                responseCode = PASSWORD_NOT_SET.first,
                description = PASSWORD_NOT_SET.second
            )
        }

        if (passwordEncoder.matches(request.newPassword, user.userPassword)) {
            return AuthResponse(
                responseCode = PASSWORD_SAME.first,
                description = PASSWORD_SAME.second
            )
        }

        user.userPassword = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)

        return AuthResponse(
            responseCode = SUCCESS.first,
            description = SUCCESS.second
        )
    }
}
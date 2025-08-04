package com.wannacry.auth_service.service

import com.wannacry.auth_service.model.User
import com.wannacry.auth_service.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun isUsernameTaken(username: String): Boolean =
        userRepository.findByUserName(username) != null

    fun saveUser(user: User): User = userRepository.save(user)

    fun saveMultipleUser(userList: List<User>) = userRepository.saveAll(userList)

    fun encodePassword(raw: String): String = passwordEncoder.encode(raw)

    fun findUserByUsername(username: String): User? =
        userRepository.findByUserName(username)

    fun isPasswordMatch(rawPassword: String, hashedPassword: String): Boolean =
        passwordEncoder.matches(rawPassword, hashedPassword)

    fun isFirstTimeLogin(user: User): Boolean =
        user.firstTimeLogin || user.userPassword.isNullOrBlank()
}
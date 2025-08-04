package com.wannacry.auth_service.model.request

data class AuthRequest<Any>(
    var data: Any
)

data class RegisterRequest(
    val email: String,
    val userName: String,
    val password: String?,
    val phoneNumber: String,
    val fullName: String,
    val role: String
)

data class LoginRequest(
    val userName: String,
    val password: String
)

data class PasswordRequest(
    val userName: String,
    val newPassword: String
)
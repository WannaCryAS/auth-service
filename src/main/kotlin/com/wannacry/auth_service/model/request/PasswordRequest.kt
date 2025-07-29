package com.wannacry.auth_service.model.request

data class PasswordRequest(
    val userName: String,
    val newPassword: String
)

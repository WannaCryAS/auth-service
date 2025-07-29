package com.wannacry.auth_service.model.response

data class AuthResponse(
    val responseCode: String,
    val description: String,
    val data: Any? = null
)

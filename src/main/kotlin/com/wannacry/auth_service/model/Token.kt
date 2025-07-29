package com.wannacry.auth_service.model

data class Token(
    val accessToken: String,
    val refreshToken: String
)

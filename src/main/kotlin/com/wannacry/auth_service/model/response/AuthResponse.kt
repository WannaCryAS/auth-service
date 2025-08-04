package com.wannacry.auth_service.model.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthResponse(
    val responseCode: String,
    val description: String,
    val responseData: Any? = null
)

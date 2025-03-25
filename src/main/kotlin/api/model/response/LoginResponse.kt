package com.helcode.api.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val token: String,
    val username: String
)
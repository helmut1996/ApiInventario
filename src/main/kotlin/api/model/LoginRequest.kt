package com.helcode.api.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val usuario: String, val contrasena: String)
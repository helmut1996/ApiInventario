package com.helcode.api.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val message: String)
package com.helcode.api.services

import kotlinx.serialization.Serializable

@Serializable
data class GenericRespose<out T>(val isSuccess:Boolean, val data:T)

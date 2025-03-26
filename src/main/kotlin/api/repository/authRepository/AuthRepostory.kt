package com.helcode.api.repository.authRepository

import com.helcode.api.model.Usuarios

interface AuthRepostory {
    suspend fun getUserByUsername(username: String): Usuarios?

}
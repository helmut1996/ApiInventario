package com.helcode.api.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.helcode.api.model.Usuarios

import io.ktor.server.config.*
import java.util.*

class AuthService(private val config: ApplicationConfig) {

    private val secret = config.property("jwt.secret").getString()
    private val issuer = config.property("jwt.issuer").getString()
    private val audience = config.property("jwt.audience").getString()
    private val expiration = 3600 * 1000 // 1 hora

    fun generateToken(user: Usuarios): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(user.usuario)
            .withClaim("idUsuario", user.idUsuario)
            .withExpiresAt(Date(System.currentTimeMillis() + expiration))
            .sign(Algorithm.HMAC256(secret))
    }
}
package com.helcode.api.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

fun generateToken(username: String, secret: String, issuer: String, audience: String): String {
    val token = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", username)
        //.withExpiresAt(Date(System.currentTimeMillis() + 600000)) // Expira en 10 minutos
        .withExpiresAt(Date(System.currentTimeMillis() + 86400000)) //Expira en 1 día
        //.withExpiresAt(Date(System.currentTimeMillis() + 3600000)) // Expira en 1 hora
        .sign(Algorithm.HMAC256(secret))
    return token
}
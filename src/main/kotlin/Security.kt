package com.helcode

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.helcode.api.model.response.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    // Leer la configuración JWT desde el archivo de configuración
    val jwtConfig = environment.config.config("jwt")
    val jwtAudience = jwtConfig.property("audience").getString()
    val jwtIssuer = jwtConfig.property("issuer").getString()
    val jwtRealm = jwtConfig.property("realm").getString()
    val jwtSecret = jwtConfig.property("secret").getString()

    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtSecret))
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .build()

    authentication {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(verifier)
            validate { credential ->
                try {
                    // No necesitas verificar el token aquí, ya lo hace Ktor
                    // verifier.verify(credential.token) // Esta línea se elimina
                    if (credential.payload.audience.contains(jwtAudience)) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                } catch (e: TokenExpiredException) {
                    println("Token expirado: ${e.message}")
                    null
                } catch (e: JWTVerificationException) {
                    println("Token invalido: ${e.message}")
                    null
                }
            }
            challenge { _, _ ->
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                try {
                    if (token != null) {
                        verifier.verify(token)
                    }
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse(message = "Token invalido"))
                } catch (e: TokenExpiredException) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse(message = "Token expirado"))
                } catch (e: JWTVerificationException) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse(message = "Token invalido"))
                }
            }
        }
    }
}


package com.helcode.api.routing

import at.favre.lib.crypto.bcrypt.BCrypt
import com.helcode.api.Database.DBConnection
import com.helcode.api.model.LoginRequest
import com.helcode.api.repository.authRepository.AuthRepositoryImpl
import com.helcode.api.repository.authRepository.AuthRepostory
import com.helcode.api.security.generateToken
import com.helcode.api.services.response.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database


fun Application.routeAuth(secret: String, issuer: String, audience: String, realm: String) {
    val db: Database = DBConnection.getDatabaseInstance()
    val authRepository: AuthRepostory = AuthRepositoryImpl(db)

    routing {
        post("/login") {
            val loginRequest = call.receive<LoginRequest>() // Recibe usuario y contraseña
            val user = authRepository.getUserByUsername(loginRequest.usuario)

            if (user != null) {
                // Verificar la contraseña usando BCrypt
                val result = BCrypt.verifyer().verify(loginRequest.contrasena.toCharArray(), user.contrasena)
                if (result.verified) {
                    val token = generateToken(user.usuario!!, secret, issuer, audience)
                    call.respond(HttpStatusCode.OK, LoginResponse(
                        message = "Inicio de sesión exitoso",
                        token = token,
                        username = user.usuario
                    )
                    )
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Credenciales inválidas")
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Usuario no encontrado")
            }
        }
    }
}

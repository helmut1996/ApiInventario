package com.helcode.api.routing

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.helcode.api.Database.DBConnection
import com.helcode.api.Database.Entity.EntityUsuarios
import com.helcode.api.model.LoginRequest
import com.helcode.api.model.Usuarios
import com.helcode.api.security.generateToken
import com.helcode.api.security.getUserByUsername
import com.helcode.api.services.response.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.util.*

fun Application.routeAuth(secret: String, issuer: String, audience: String, realm: String) {
    val db: Database = DBConnection.getDatabaseInstance()

    routing {
        post("/login") {
            val loginRequest = call.receive<LoginRequest>() // Recibe usuario y contraseña
            val user = getUserByUsername(db, loginRequest.usuario)

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

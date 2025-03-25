package com.helcode

import com.helcode.api.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*




fun Application.configureRouting(secret: String, issuer: String, audience: String, realm: String) {
    routing {
        routeAuth(secret, issuer, audience, realm)
        routeEstado()
        routePermiso()
        authenticate("auth-jwt") { // You're referencing "auth-jwt" here
            get("/protected") {
                call.respondText("Ruta Protegida")
            }
        }
        // ... (other routes) ...
        routeProvedor()
        routeProducto()
        routeUsuarios()
    }
}


package com.helcode

import com.helcode.api.routing.*
import com.helcode.api.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting(authService: AuthService) {
    routing {
        authenticate {
            routeProvedor()
            routeEstado()
            routePermiso()
            routeProducto()
            routeUsuarios()
        }

    }



}

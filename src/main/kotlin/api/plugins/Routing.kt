package com.helcode

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.helcode.api.routing.routeEstado
import com.helcode.api.routing.routePermiso
import com.helcode.api.routing.routeProducto
import com.helcode.api.routing.routeProvedor
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routeProvedor()
    routeEstado()
    routePermiso()
    routeProducto()
}

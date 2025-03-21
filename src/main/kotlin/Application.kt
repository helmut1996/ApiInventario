package com.helcode

import com.helcode.api.services.AuthService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val authService = AuthService(environment.config)
    configureSerialization()
    configureSecurity()
    configureRouting(authService)
}

package com.helcode

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val jwtConfig = config.config("jwt")
    val secret = jwtConfig.property("secret").getString()
    val issuer = jwtConfig.property("issuer").getString()
    val audience = jwtConfig.property("audience").getString()
    val realm = jwtConfig.property("realm").getString()

    configureSerialization()
    configureSecurity()
    configureRouting(secret, issuer, audience, realm)
}

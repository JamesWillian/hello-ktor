package com.jammes

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

val jwtSecret = System.getenv("JWT_SECRET") ?: throw IllegalStateException("JWT_SECRET not found at environment variables")

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureRouting()
}

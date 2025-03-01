package com.jammes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.jammes.database.DatabaseFactory
import com.jammes.routes.eventRoutes
import com.jammes.routes.userRoutes
import com.jammes.schemas.EventService
import com.jammes.schemas.UserService
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/inicio") {
            call.respondText("Pagina Inicial!")
        }
    }

    val database = DatabaseFactory.getDatabase()
    val userService = UserService(database)
    val eventService = EventService(database)

    routing {
        userRoutes(userService)
        eventRoutes(eventService)
    }
}

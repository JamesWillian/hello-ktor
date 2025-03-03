package com.jammes

import com.jammes.routes.eventRoutes
import com.jammes.routes.userRoutes
import com.jammes.schemas.EventService
import com.jammes.schemas.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/inicio") {
            call.respondText("Pagina Inicial!")
        }
    }

    val userService = UserService()
    val eventService = EventService()

    routing {
        userRoutes(userService)
        eventRoutes(eventService)
    }
}

package com.jammes

import com.jammes.routes.authRoutes
import com.jammes.routes.eventRegistrationRoutes
import com.jammes.routes.eventRoutes
import com.jammes.routes.userRoutes
import com.jammes.services.EventService
import com.jammes.services.UserService
import com.jammes.services.EventRegistrationService
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
    val eventRegistrationService = EventRegistrationService()

    routing {
        authRoutes()
        userRoutes(userService)
        eventRoutes(eventService)
        eventRegistrationRoutes(eventRegistrationService)
    }
}

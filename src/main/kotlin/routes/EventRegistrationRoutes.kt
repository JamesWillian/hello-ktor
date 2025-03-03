package com.jammes.routes

import com.jammes.services.EventRegistrationService
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.eventRegistrationRoutes(service: EventRegistrationService) {
    post("/events/{eventId}/register/{userId}") {
        val eventId = call.parameters["eventId"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val userId = call.parameters["userId"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
//        val userId = call.receive<UserIdRequest>().userId
        service.registerUser(userId, eventId)
        call.respond(HttpStatusCode.Created)
    }

    delete("/events/{eventId}/unregister/{userId}") {
        val eventId = call.parameters["eventId"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val userId = call.parameters["userId"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
//        val userId = call.receive<UserIdRequest>().userId
        service.unregisterUser(userId, eventId)
        call.respond(HttpStatusCode.OK)
    }

    get("/events/{eventId}/users") {
        val eventId = call.parameters["eventId"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val users = service.getUsersByEvent(eventId)
        call.respond(users)
    }

    get("/users/{userId}/events") {
        val userId = call.parameters["userId"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val events = service.getEventsByUser(userId)
        call.respond(events)
    }
}

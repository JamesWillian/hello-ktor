package com.jammes.routes

import com.jammes.schemas.EventService
import com.jammes.schemas.ExposedEvent
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.eventRoutes(eventService: EventService) {
    route("/events") {
        post {
            val event = call.receive<ExposedEvent>()
            val id = eventService.create(event)
            call.respond(HttpStatusCode.Created, id)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val event = eventService.read(id)
            if (event != null) {
                call.respond(HttpStatusCode.OK, event)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get {
            val event = call.request.queryParameters["event"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val startAt = call.request.queryParameters["startAt"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val endAt = call.request.queryParameters["endAt"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val eventList = eventService.readAll(event, startAt, endAt)
            call.respond(HttpStatusCode.OK, eventList)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val event = call.receive<ExposedEvent>()
            eventService.update(id, event)
            call.respond(HttpStatusCode.OK)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            eventService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
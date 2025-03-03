package com.jammes.routes

import com.jammes.models.EventRequest
import com.jammes.services.EventService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.eventRoutes(eventService: EventService) {
    authenticate {
        route("/events") {
            post {
                val event = call.receive<EventRequest>()
                val id = eventService.create(event)
                call.respond(HttpStatusCode.Created, id)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "ID inválido"
                )
                val event = eventService.read(id)
                if (event != null) {
                    call.respond(HttpStatusCode.OK, event)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get {
                val event = call.request.queryParameters["event"]
                val startAt = call.request.queryParameters["startAt"]
                val endAt = call.request.queryParameters["endAt"]
                val eventList = eventService.readAll(event, startAt, endAt)
                call.respond(HttpStatusCode.OK, eventList)
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    "ID inválido"
                )
                val event = call.receive<EventRequest>()
                eventService.update(id, event)
                call.respond(HttpStatusCode.OK)
            }

            patch("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respond(
                    HttpStatusCode.BadRequest,
                    "ID inválido"
                )
                val eventUpdates = call.receive<EventRequest>()

                eventService.updatePartial(id, eventUpdates)
                call.respond(HttpStatusCode.OK)
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    "ID inválido"
                )
                eventService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
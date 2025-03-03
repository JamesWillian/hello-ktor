package com.jammes.routes

import com.jammes.models.UserRequest
import com.jammes.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        // Create user
        post {
            val user = call.receive<UserRequest>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }

        authenticate {
            // Read user
            get("/{id}") {
                val id =
                    call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            // Update user
            put("/{id}") {
                val id =
                    call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = call.receive<UserRequest>()
                userService.update(id, user)
                call.respond(HttpStatusCode.OK)
            }

            patch("/changePassword") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.payload?.subject
                val id = userService.findUserByEmail(email!!)?.id ?: throw IllegalArgumentException("Invalid User")
                val newPassword = call.receive<String>()

                userService.changePassword(id, newPassword)
                call.respond(HttpStatusCode.OK)
            }

            // Delete user
            delete("/{id}") {
                val id =
                    call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                userService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
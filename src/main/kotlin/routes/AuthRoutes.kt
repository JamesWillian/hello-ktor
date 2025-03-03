package com.jammes.routes

import com.jammes.AuthService
import com.jammes.services.UserService
import com.jammes.verifyPassword
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable

fun Route.authRoutes() {
    post("/login") {
        val credentials = call.receive<UserCredentials>()
        val user = UserService().findUserByEmail(credentials.email)

        if ( user != null && verifyPassword(credentials.password, user.password.toString())) {
            val token = AuthService.generateToken(credentials.email)
            call.respond(mapOf("token" to token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
        }
    }
}

@Serializable
data class UserCredentials(val email: String, val password: String)

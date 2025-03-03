package com.jammes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object AuthService {
    private const val issuer = "ktor-server"
    private const val audience = "ktor_users"
    private const val expirationTime = 3600 * 1000

    fun generateToken(username: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}
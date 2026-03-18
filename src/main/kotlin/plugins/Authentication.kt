package plugins


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "ktor-app"
            verifier(
                JWT.require(Algorithm.HMAC256("my-super-secret-key"))
                    .withAudience("mobile-app")
                    .withIssuer("ktor-app")
                    .build()
            )
            validate { credential ->
                val username = credential.payload.getClaim("username").asString()
                val exp = credential.payload.expiresAt?.time ?: 0
                val now = System.currentTimeMillis()
                val expired = exp < now

                println("JWT validate called:")
                println("  username: $username")
                println("  exp: $exp")
                println("  now: $now")
                println("  expired: $expired")

                if (username != null && !expired) {
                    println("JWT валидный")
                    JWTPrincipal(credential.payload)
                } else {
                    println("JWT невалидный")
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid or expired token"))
            }
        }
    }
}
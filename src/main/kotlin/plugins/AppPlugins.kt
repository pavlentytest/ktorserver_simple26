package plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import data.repository.AuthRepositoryImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun Application.configurePlugins() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        })
    }
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]?.take(50) ?: "Unknown"
            val path = call.request.path()
            "[$status] $httpMethod $path - UA: $userAgent"
        }
    }
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "ktor-app"
            verifier(
                JWT.require(Algorithm.HMAC256(AuthRepositoryImpl.SECRET))
                    .withAudience(AuthRepositoryImpl.AUDIENCE)
                    .withIssuer(AuthRepositoryImpl.ISSUER)
                    .build()
            )
            validate { credential ->
                val username = credential.payload.getClaim("username").asString()
                if (username != null) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid or expired token"))
            }
        }
    }
    println("Плагины настроены!")
}


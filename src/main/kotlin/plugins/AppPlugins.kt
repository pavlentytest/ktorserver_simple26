package plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
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
    println("Плагины настроены: JSON, логирование")
}


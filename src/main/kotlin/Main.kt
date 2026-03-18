package org.example

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.event.Level


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
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
            val userAgent = call.request.headers["User-Agent"]
            val path = call.request.path()
            "[$status] $httpMethod $path - UA: $userAgent"
        }
    }

    routing {
        get("/") {
            call.respondText("First Ktor Server! 🚀")
        }

        get("/hello/{name?}") {
            val name = call.parameters["name"] ?: "Гость"
            call.respondText("Привет, $name! Как дела?")
        }

        get("/user") {
            val user = User(
                id = 1,
                name = "Алексей",
                age = 25,
                isStudent = true,
                skills = listOf("Kotlin", "Android", "Ktor")
            )
            call.respond(user)
        }

        get("/users") {
            val users = listOf(
                User(1, "Алексей", 25, true, listOf("Kotlin", "Android")),
                User(2, "Мария", 22, true, listOf("Swift", "iOS")),
                User(3, "Дмитрий", 30, false, listOf("Java", "Spring"))
            )
            call.respond(users)
        }

        post("/users") {
            val newUser = call.receive<User>()
            call.respond(HttpStatusCode.Created, newUser.copy(id = 100))
        }

        get("/error") {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Ты специально сюда зашёл? 😄"))
        }
    }
}


@Serializable
data class User(
    val id: Int,
    val name: String,
    val age: Int,
    val isStudent: Boolean,
    val skills: List<String>
)
package presentation

import domain.model.User
import domain.usecase.GetGreetingUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

class GreetingController(
    private val getGreetingUseCase: GetGreetingUseCase
) {
    fun configureRoutes(application: Application) {
        application.routing {
            get("/") {
                call.respondText("Добро пожаловать на Ktor-сервер! 🚀")
            }

            get("/hello") {
                val name = call.parameters["name"]
                val greeting = getGreetingUseCase.createGreeting(name)
                call.respond(greeting)
            }

            get("/special") {
                val greeting = getGreetingUseCase.getSpecialGreeting()
                call.respond(greeting)
            }
            post("/users") {
                val newUser = call.receive<User>()
                val createdUser = newUser.copy(id = 100)
                call.respond(HttpStatusCode.Created, createdUser)
            }
        }
    }
}
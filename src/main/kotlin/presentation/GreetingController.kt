package presentation

import domain.model.FakeUserDetail
import domain.usecase.GetGreetingUseCase
import domain.usecase.LoginUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val token: String)

class GreetingController(
    private val getGreetingUseCase: GetGreetingUseCase,
    private val loginUseCase: LoginUseCase
) {
    fun configureRoutes(application: Application) {
        application.routing {
            get("/") {
                call.respondText("Добро пожаловать на Ktor-сервер! 🚀")
            }

            post("/login") {
                val request = call.receive<LoginRequest>()
                val token = loginUseCase.login(request.username, request.password)

                if (token != null) {
                    call.respond(LoginResponse(token = token))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Неверный логин или пароль"))
                }
            }

            authenticate("auth-jwt") {
                get("/hello") {
                    val name = call.parameters["name"]
                    val greeting = getGreetingUseCase.createGreeting(name)
                    call.respond(greeting)
                }

                get("/special") {
                    val greeting = getGreetingUseCase.getSpecialGreeting()
                    call.respond(greeting)
                }
            }

            post("/users") {
                val newUser = call.receive<FakeUserDetail>()
                val createdUser = newUser.copy(id = 100)
                call.respond(HttpStatusCode.Created, createdUser)
            }
        }
    }
}
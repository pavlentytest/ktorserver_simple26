package presentation

import data.dto.UserResponseDto
import di.AppContainer.getUserUseCase
import domain.usecase.GetUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal


import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class UserController(
    private val getUserUseCase: GetUserUseCase
) {
    fun configure(application: Application) {
        application.routing {
            authenticate("auth-jwt") {
                get("/users/me") {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val user = getUserUseCase(username)

                    if (user != null) {
                        val dto = UserResponseDto(
                            id = user.id,
                            username = user.username,
                            role = user.role
                        )
                        call.respond(dto)
                    } else {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Пользователь не найден"))
                    }
                }
            }
        }
    }
}
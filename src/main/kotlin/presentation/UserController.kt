package presentation

import data.dto.CountryResponseDto
import data.dto.UserResponseDto
import di.AppContainer.getUserUseCase
import domain.usecase.GetUserCountriesUseCase
import domain.usecase.GetUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal


import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jdk.internal.net.http.common.Log

class UserController(
    private val getUserUseCase: GetUserUseCase,
    private val getUserCountriesUseCase: GetUserCountriesUseCase
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

                get("/users/me/countries") {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val user = getUserUseCase(username)

                    if (user == null) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Пользователь не найден"))
                        return@get
                    }

                    val countriesResult = getUserCountriesUseCase(user.id)

                    countriesResult.fold(
                        onSuccess = { countries ->
                            val dtos = countries.map { country ->
                                CountryResponseDto(
                                    id = country.id,
                                    name = country.name,
                                    code = country.code,
                                    visitedAt = country.visitedAt
                                )
                            }
                            call.respond(HttpStatusCode.OK, dtos)
                        },
                        onFailure = { e ->
                            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Неизвестная ошибка")))
                        }
                    )
                }
            }
        }
    }
}

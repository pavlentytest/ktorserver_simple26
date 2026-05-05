package presentation

import data.dto.CountryResponseDto
import data.dto.UserResponseDto
import domain.usecase.GetUserCountriesUseCase
import domain.usecase.GetUserUseCase
import io.github.smiley4.ktoropenapi.get
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
                get("/users/me", {
                    tags = listOf("Users")
                    summary = "Получить профиль текущего пользователя"
                    securitySchemeNames = listOf("MyJwtAuth")
                    response {
                        HttpStatusCode.OK to {
                            description = "Профиль пользователя"
                            body<UserResponseDto>()
                        }
                        HttpStatusCode.NotFound to {
                            description = "Пользователь не найден"
                        }
                    }
                }) {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val user = getUserUseCase(username)

                    if (user != null) {
                        call.respond(UserResponseDto(user.id, user.username, user.role))
                    } else {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "Пользователь не найден"))
                    }
                }
                get("/users/me/countries", {
                    tags = listOf("Users", "Countries")
                    summary = "Список посещенных стран пользователя"
                    securitySchemeNames = listOf("MyJwtAuth")
                    response {
                        HttpStatusCode.OK to {
                            description = "Список стран"
                            body<List<CountryResponseDto>>()
                        }
                        HttpStatusCode.InternalServerError to {
                            description = "Ошибка сервера"
                        }
                    }
                }) {
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

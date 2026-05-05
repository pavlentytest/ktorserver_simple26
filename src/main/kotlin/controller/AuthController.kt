package controller

import domain.usecase.LoginUseCase
import io.github.smiley4.ktoropenapi.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val token: String)

@Serializable
data class ErrorResponse(val error: String, val code: Int? = null)

class AuthController(
    private val loginUseCase: LoginUseCase
) {
    fun configure(application: Application) {
        application.routing {
            post("/login", {
                tags = listOf("Authentication")
                summary = "Авторизация пользователя"
                description = "Принимает логин/пароль и возвращает JWT токен"

                request {
                    body<LoginRequest> {
                        description = "Данные для входа"
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Успешный вход"
                        body<LoginResponse>()
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "Неверные учетные данные"
                    }
                }
            }) {
                val request = call.receive<LoginRequest>()
                val token = loginUseCase.login(request.username, request.password)

                if (token != null) {
                    call.respond(LoginResponse(token))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Неверный логин или пароль"))
                }
            }
        }
    }
}
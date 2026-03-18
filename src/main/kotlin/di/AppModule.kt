package di


import data.repository.UserRepositoryImpl
import domain.repository.UserRepository
import domain.usecase.GetUserUseCase
import domain.usecase.LoginUseCase
import io.ktor.server.application.Application
import presentation.AuthController
import presentation.UserController
import security.PasswordHasher
import kotlin.getValue

object AppContainer {
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val loginUseCase: LoginUseCase by lazy { LoginUseCase(userRepository, PasswordHasher) }
    val getUserUseCase: GetUserUseCase by lazy { GetUserUseCase(userRepository) }

    val authController: AuthController by lazy { AuthController(loginUseCase) }
    val userController: UserController by lazy { UserController(getUserUseCase) }
}

fun Application.appModule() {
    println("DI инициализирован")
}
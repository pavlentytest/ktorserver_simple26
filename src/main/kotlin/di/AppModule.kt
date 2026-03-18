package di


import data.repository.AuthRepositoryImpl
import data.repository.GreetingRepositoryImpl
import domain.repository.AuthRepository
import domain.repository.GreetingRepository
import domain.usecase.GetGreetingUseCase
import domain.usecase.LoginUseCase
import presentation.GreetingController
import kotlin.getValue

object AppContainer {
    val greetingRepository: GreetingRepository by lazy { GreetingRepositoryImpl() }
    val authRepository: AuthRepository by lazy { AuthRepositoryImpl() }

    val getGreetingUseCase: GetGreetingUseCase by lazy { GetGreetingUseCase(greetingRepository) }
    val loginUseCase: LoginUseCase by lazy { LoginUseCase(authRepository) }

    val greetingController: GreetingController by lazy {
        GreetingController(getGreetingUseCase, loginUseCase)
    }
}
fun appModule() {
    println("DI инициализирован!")
}
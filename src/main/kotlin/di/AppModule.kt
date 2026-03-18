package di


import data.repository.GreetingRepositoryImpl
import domain.repository.GreetingRepository
import domain.usecase.GetGreetingUseCase
import presentation.GreetingController

object AppContainer {
    val greetingRepository: GreetingRepository by lazy { GreetingRepositoryImpl() }
    val getGreetingUseCase: GetGreetingUseCase by lazy { GetGreetingUseCase(greetingRepository) }
    val greetingController: GreetingController by lazy { GreetingController(getGreetingUseCase) }
}

fun appModule() {
    println("DI инициализирован!")
}
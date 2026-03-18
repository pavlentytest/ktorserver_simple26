package domain.usecase

import domain.model.Greeting
import domain.repository.GreetingRepository

class GetGreetingUseCase(
    private val repository: GreetingRepository
) {
    fun createGreeting(name: String? = null): Greeting {
        return repository.createGreeting(name)
    }

    fun getSpecialGreeting(): Greeting {
        return repository.getSpecialGreeting()
    }
}
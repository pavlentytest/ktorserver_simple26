package data.repository

import domain.model.Greeting
import domain.repository.GreetingRepository


class GreetingRepositoryImpl : GreetingRepository {

    override fun createGreeting(name: String?): Greeting {
        val message = if (name != null) {
            "Привет, $name! Добро пожаловать в Ktor 😄"
        } else {
            "Привет, Noname!"
        }
        return Greeting(message = message, name = name)
    }

    override fun getSpecialGreeting(): Greeting {
        return Greeting(
            message = "Это специальное приветствие от сервиса!",
            name = "Важный гость"
        )
    }
}
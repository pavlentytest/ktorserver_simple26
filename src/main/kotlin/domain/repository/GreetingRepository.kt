package domain.repository

import domain.model.Greeting

interface GreetingRepository {
    fun createGreeting(name: String? = null): Greeting
    fun getSpecialGreeting(): Greeting
}
package domain.repository

import domain.model.Country
import domain.model.User

interface UserRepository {
    suspend fun findByUsername(username: String): User?
    suspend fun createUser(username: String, passwordHash: String, role: String = "user"): Int
    suspend fun findById(id: Int): User?

    suspend fun getUserCountries(userId: Int): List<Country>
}
package domain.usecase

import domain.model.User
import domain.repository.UserRepository


class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String): User? {
        return userRepository.findByUsername(username)
    }
    suspend fun getById(id: Int): User? {
        return userRepository.findById(id)
    }
}
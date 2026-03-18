package domain.usecase

import domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    fun login(username: String, password: String): String? {
        return authRepository.authenticate(username, password)
    }
}
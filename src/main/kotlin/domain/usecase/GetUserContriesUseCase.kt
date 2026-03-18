package domain.usecase

import domain.model.Country
import domain.repository.UserRepository

class GetUserCountriesUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<Country>> {
        return runCatching {
            repository.getUserCountries(userId)
        }
    }
}
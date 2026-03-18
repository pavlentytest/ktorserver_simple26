package domain.model


data class Country(
    val id: Int,
    val name: String,
    val code: String,
    val visitedAt: Long
)
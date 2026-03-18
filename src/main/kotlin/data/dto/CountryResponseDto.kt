package data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CountryResponseDto(
    val id: Int,
    val name: String,
    val code: String,
    val visitedAt: Long
)
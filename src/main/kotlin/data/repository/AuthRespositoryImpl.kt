package data.repository

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import domain.model.AuthUser
import domain.repository.AuthRepository
import java.util.Date

class AuthRepositoryImpl : AuthRepository {

    private val secret = "my-super-secret-key-32-chars-minimum"  // to config!!!
    private val issuer = "ktor-app"
    private val audience = "mobile-app"

    private val users = listOf(
        AuthUser("admin", "123", "admin"),
        AuthUser("user", "1234", "user")
    )

    override fun authenticate(username: String, password: String): String? {
        val user = users.find { it.username == username && it.password == password }
            ?: return null

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("role", user.role)
            .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000))
            .sign(Algorithm.HMAC256(secret))
    }

    companion object {
        const val SECRET = "my-super-secret-key-32-chars-minimum"
        const val ISSUER = "ktor-app"
        const val AUDIENCE = "mobile-app"
    }
}
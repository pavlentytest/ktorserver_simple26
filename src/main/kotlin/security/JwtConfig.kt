package security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import java.util.Date

object JwtConfig {
    private const val SECRET = "my-super-secret-key"
    private const val ISSUER = "ktor-app"
    private const val AUDIENCE = "mobile-app"
    private const val VALIDITY = 7L * 24 * 60 * 60 * 1000 // 7
    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(SECRET))
        .withAudience(AUDIENCE)
        .withIssuer(ISSUER)
        .build()

    fun generateToken(username: String, role: String): String {
        return JWT.create()
            .withAudience(AUDIENCE)
            .withIssuer(ISSUER)
            .withClaim("username", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY))
            .sign(Algorithm.HMAC256(SECRET))
    }
}
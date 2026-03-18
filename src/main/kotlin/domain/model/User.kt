package domain.model


data class User(
    val id: Int,
    val username: String,
    val role: String,
) {
    companion object {
        val GUEST = User(id = 0, username = "guest", role = "guest")
    }
}
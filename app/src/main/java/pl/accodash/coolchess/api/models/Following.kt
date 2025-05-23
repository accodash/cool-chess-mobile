package pl.accodash.coolchess.api.models

data class Following(
    val id: String,
    val follower: User? = null,
    val followedUser: User? = null
)

package pl.accodash.coolchess.api.models

data class FriendRelation(
    val id: String,
    val firstUser: User,
    val secondUser: User,
    val createdAt: String,
    val befriendedAt: String? = null
)

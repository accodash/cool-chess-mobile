package pl.accodash.coolchess.api.models

data class Rating(
    val id: String,
    val rating: Int,
    val mode: String,
    val user: User? = null
)

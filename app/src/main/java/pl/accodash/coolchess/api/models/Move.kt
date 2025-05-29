package pl.accodash.coolchess.api.models

data class Move(
    val id: String,
    val from: String,
    val to: String,
    val movedAt: String,
    val timeLeft: Int
)

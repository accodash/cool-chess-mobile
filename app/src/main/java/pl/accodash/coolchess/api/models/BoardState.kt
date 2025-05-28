package pl.accodash.coolchess.api.models

data class BoardState(
    val board: String,
    val remainingBlackTime: Long,
    val remainingWhiteTime: Long,
    val turn: String
)

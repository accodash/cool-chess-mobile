package pl.accodash.coolchess.api.models

data class Match(
    val id: String,
    val whitePlayer: User,
    val blackPlayer: User,
    val startAt: String,
    val winner: User?,
    val isCompleted: Boolean,
    val endAt: String?,
    val followers: List<String>?,
    val mode: String,
    val isRanked: Boolean,
    val moves: List<Map<String, Any>>,
    val boardState: BoardState
)

data class MoveInfo(
    val to: String,
    val color: String
)

typealias MovesBySquare = Map<String, List<MoveInfo>>

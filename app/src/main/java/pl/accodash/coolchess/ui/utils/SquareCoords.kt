package pl.accodash.coolchess.ui.utils

fun getSquareCoords(square: String, color: String): Pair<Int, Int>? {
    if (square.length != 2) return null
    val file = square[0]
    val rank = square[1].digitToIntOrNull() ?: return null

    val col = "abcdefgh".indexOf(file)
    val row = 8 - rank

    return if (color == "white") {
        row to col
    } else {
        (7 - row) to (7 - col)
    }
}

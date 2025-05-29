package pl.accodash.coolchess.ui.utils

import pl.accodash.coolchess.R

val pieceResourceMap = mapOf(
    'r' to R.drawable.rb,
    'n' to R.drawable.nb,
    'b' to R.drawable.bb,
    'q' to R.drawable.qb,
    'k' to R.drawable.kb,
    'p' to R.drawable.pb,
    'R' to R.drawable.rw,
    'N' to R.drawable.nw,
    'B' to R.drawable.bw,
    'Q' to R.drawable.qw,
    'K' to R.drawable.kw,
    'P' to R.drawable.pw,
)

fun parseFEN(fen: String?, color: String): List<List<Int?>>? {
    if (fen.isNullOrBlank() || !fen.contains(" ")) return null

    val piecePlacement = fen.split(" ")[0]
    val rows = piecePlacement.split("/")

    val board = mutableListOf<MutableList<Int?>>()

    for (rowStr in rows) {
        val row = mutableListOf<Int?>()
        for (char in rowStr) {
            if (char.isDigit()) {
                repeat(char.digitToInt()) {
                    row.add(null)
                }
            } else {
                row.add(pieceResourceMap[char])
            }
        }
        board.add(row)
    }

    return if (color == "white") {
        board
    } else {
        board.reversed().map { it.reversed().toMutableList() }
    }
}

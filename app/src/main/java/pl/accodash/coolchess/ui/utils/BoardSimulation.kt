package pl.accodash.coolchess.ui.utils

import pl.accodash.coolchess.api.models.Move

fun cloneBoard(board: List<List<Int?>>): List<MutableList<Int?>> {
    return board.map { it.toMutableList() }
}

fun applyMoves(board: List<List<Int?>>, moves: List<Move>): List<MutableList<Int?>> {
    val newBoard = cloneBoard(board)

    for (move in moves) {
        val fromCol = move.from[0] - 'a'
        val fromRow = 8 - move.from[1].digitToInt()
        val toCol = move.to[0] - 'a'
        val toRow = 8 - move.to[1].digitToInt()

        newBoard[toRow][toCol] = newBoard[fromRow][fromCol]
        newBoard[fromRow][fromCol] = null
    }

    return newBoard
}

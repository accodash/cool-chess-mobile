package pl.accodash.coolchess.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ChessBoard(
    board: List<List<Int?>>,
    userColor: String,
    modifier: Modifier = Modifier,
    highlightedSquares: Set<String> = emptySet(),
    onSquareClick: ((colIndex: Int, rowIndex: Int, hasPiece: Boolean) -> Unit)? = null
) {
    val letters = if (userColor == "white") "abcdefgh" else "hgfedcba"
    val rows = if (userColor == "white") board else board.reversed()

    Box(modifier = modifier.aspectRatio(1f)) {
        Column(modifier = Modifier.fillMaxSize()) {
            rows.forEachIndexed { rowIndex, row ->
                Row(modifier = Modifier.weight(1f)) {
                    val actualRow = if (userColor == "white") row else row.reversed()

                    actualRow.forEachIndexed { colIndex, pieceResId ->
                        val isDark = (rowIndex + colIndex) % 2 == 1
                        val rank = if (userColor == "white") 8 - rowIndex else rowIndex + 1
                        val file = letters[colIndex]
                        val square = "$file$rank"
                        val isHighlighted = square in highlightedSquares

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when {
                                        isHighlighted -> Color(0xFFBACA44)
                                        isDark -> Color(0xFF769656)
                                        else -> Color(0xFFEEEED2)
                                    }
                                )
                                .clickable(enabled = onSquareClick != null) {
                                    onSquareClick?.invoke(colIndex, rowIndex, pieceResId != null)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            pieceResId?.let {
                                Image(
                                    painter = painterResource(id = it),
                                    contentDescription = "chess piece",
                                    modifier = Modifier.fillMaxSize(0.85f),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

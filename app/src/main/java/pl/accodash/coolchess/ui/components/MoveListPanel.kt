package pl.accodash.coolchess.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.models.Move
import pl.accodash.coolchess.ui.utils.applyMoves
import pl.accodash.coolchess.ui.utils.cloneBoard
import pl.accodash.coolchess.ui.utils.getSquareCoords
import pl.accodash.coolchess.ui.utils.parseFEN

@Composable
fun MoveListPanel(
    moves: List<Move>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    userColor: String,
    modifier: Modifier = Modifier
) {
    val initialBoard =
        parseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", userColor) ?: return
    val boardStates = mutableListOf<List<List<Int?>>>()
    var currentBoard = cloneBoard(initialBoard)

    for (i in moves.indices) {
        boardStates.add(currentBoard)
        currentBoard = applyMoves(currentBoard, listOf(moves[i]))
    }

    Card(
        modifier = modifier
            .fillMaxHeight(0.8f)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                stringResource(R.string.moves_made),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(12.dp)
            )
            moves.forEachIndexed { index, move ->
                val board = boardStates[index]
                val (row, col) = getSquareCoords(move.from, userColor) ?: (0 to 0)
                val piece = board.getOrNull(row)?.getOrNull(col)

                val backgroundColor =
                    if (index == selectedIndex) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else Color.Transparent

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .clickable { onSelect(index) }
                        .padding(8.dp)
                ) {
                    piece?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(move.from, style = MaterialTheme.typography.bodyLarge)
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(
                            R.string.to
                        )
                    )
                    Text(
                        move.to,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.width(60.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = formatTime(move.timeLeft * 1000),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

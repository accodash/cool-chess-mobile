package pl.accodash.coolchess.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.Match
import pl.accodash.coolchess.api.models.Move
import pl.accodash.coolchess.ui.components.ChessBoard
import pl.accodash.coolchess.ui.components.MatchCard
import pl.accodash.coolchess.ui.components.MoveListPanel
import pl.accodash.coolchess.ui.utils.applyMoves
import pl.accodash.coolchess.ui.utils.parseFEN

@Composable
fun MatchHistoryScreen(
    matchId: String,
    services: CoolChessServices,
    modifier: Modifier = Modifier
) {
    var match by remember { mutableStateOf<Match?>(null) }
    var moves by remember { mutableStateOf<List<Move>>(emptyList()) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var showMoveList by remember { mutableStateOf(false) }
    var currentUserId by remember { mutableStateOf<String?>(null) }

    val startingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

    val initialBoard = remember(match) {
        if (match == null) emptyList()
        else parseFEN(startingPosition, "white")
    }

    val currentBoard = remember(moves, selectedIndex, initialBoard) {
        if (initialBoard == null) emptyList()
        else {
            val movesToApply = if (selectedIndex >= 0) moves.take(selectedIndex + 1) else emptyList()
            applyMoves(initialBoard, movesToApply)
        }
    }

    LaunchedEffect(Unit) {
        val currentUser = services.userService.getCurrentUser()
        currentUserId = currentUser.uuid
    }

    LaunchedEffect(matchId) {
        isLoading = true
        isError = false
        try {
            match = services.matchService.fetchMatch(matchId)
            moves = services.moveService.fetchMoves(matchId)
        } catch (e: Exception) {
            isError = true
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (isError || match == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error loading match.")
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MatchCard(match = match!!, currentUserId = currentUserId, isStatic = true)

        ChessBoard(
            board = currentBoard,
            userColor = "white",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Button(
            onClick = { showMoveList = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Select Move")
        }
    }

    if (showMoveList) {
        AlertDialog(
            onDismissRequest = { showMoveList = false },
            confirmButton = {},
            text = {
                MoveListPanel(
                    moves = moves,
                    selectedIndex = selectedIndex,
                    onSelect = { index ->
                        selectedIndex = index
                        showMoveList = false
                    },
                    userColor = "white",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

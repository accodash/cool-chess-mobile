package pl.accodash.coolchess.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.Match
import pl.accodash.coolchess.ui.components.MatchCard
import pl.accodash.coolchess.ui.components.PaginationControls

@Composable
fun HistoryScreen(
    services: CoolChessServices,
    modifier: Modifier = Modifier,
    onMatchCardClick: (String) -> Unit = {}
) {
    var page by rememberSaveable { mutableIntStateOf(1) }
    val limit = 10
    val offset = (page - 1) * limit

    var matches by remember { mutableStateOf<List<Match>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var currentUserId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val currentUser = services.userService.getCurrentUser()
        currentUserId = currentUser.uuid
    }

    LaunchedEffect(page) {
        isLoading = true
        isError = false
        try {
            val fetched = services.matchService.fetchMatches(
                offset = offset,
                limit = limit + 1
            )
            matches = fetched
        } catch (e: Exception) {
            Log.e("SAD", e.message!!)
            isError = true
        } finally {
            isLoading = false
        }
    }

    val currentMatches = matches.take(limit)
    val hasNext = matches.size > limit

    Column(modifier = modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            isError -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.failed_to_load))
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(currentMatches) { match ->
                        MatchCard(match = match, currentUserId = currentUserId, onClick = onMatchCardClick)
                    }
                }

                PaginationControls(
                    page = page,
                    hasNext = hasNext,
                    onChange = { delta -> page += delta }
                )
            }
        }
    }
}

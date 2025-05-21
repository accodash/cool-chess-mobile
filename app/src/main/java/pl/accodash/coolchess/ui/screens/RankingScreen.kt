package pl.accodash.coolchess.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.Rating
import pl.accodash.coolchess.ui.components.PaginationControls
import pl.accodash.coolchess.ui.components.ModeSelector
import pl.accodash.coolchess.ui.components.UserCard

private const val LIMIT = 50

@Composable
fun RankingScreen(
    services: CoolChessServices,
    modifier: Modifier = Modifier,
    onUserClick: (String) -> Unit
) {
    var mode by rememberSaveable { mutableStateOf("bullet") }
    var page by rememberSaveable { mutableStateOf(1) }
    val offset = (page - 1) * LIMIT
    val coroutineScope = rememberCoroutineScope()

    var ratings by rememberSaveable { mutableStateOf<List<Rating>>(emptyList()) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isError by rememberSaveable { mutableStateOf(false) }

    fun fetchData() {
        coroutineScope.launch {
            isLoading = true
            isError = false
            try {
                ratings = services.ratingService.fetchRanking(mode, offset, LIMIT + 1)
            } catch (e: Exception) {
                isError = true
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(mode, page) { fetchData() }

    val hasNextPage = ratings.size > LIMIT
    val entries = ratings.take(LIMIT)

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        ModeSelector(selectedMode = mode, onSelect = {
            mode = it
            page = 1
        })

        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                isError -> {
                    Text(
                        text = stringResource(R.string.failed_to_load),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    if (entries.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.no_users_found))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(entries) { index, entry ->
                                entry.user?.let { user ->
                                    UserCard(
                                        index = offset + index + 1,
                                        username = user.username,
                                        createdAt = user.createdAt,
                                        imageUrl = user.getBackendImageUrl(),
                                        followersCount = user.followersCount,
                                        uuid = user.uuid,
                                        elo = entry.rating,
                                        onClick = onUserClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        PaginationControls(
            page = page,
            hasNext = hasNextPage,
            onChange = { delta -> page += delta }
        )
    }
}

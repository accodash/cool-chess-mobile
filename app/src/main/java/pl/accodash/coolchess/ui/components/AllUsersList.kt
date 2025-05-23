package pl.accodash.coolchess.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.api.models.User
import pl.accodash.coolchess.api.services.UserService

@Composable
fun AllUsersList(
    userService: UserService,
    onUserClick: (String) -> Unit
) {
    val limit = 50

    var page by rememberSaveable { mutableStateOf(1) }
    var sortBy by rememberSaveable { mutableStateOf("createdAt") }
    var order by rememberSaveable { mutableStateOf("DESC") }
    var search by rememberSaveable { mutableStateOf("") }

    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasNextPage by remember { mutableStateOf(false) }

    LaunchedEffect(page, sortBy, order, search) {
        isLoading = true
        try {
            val rawUsers = userService.fetchUsers(
                offset = (page - 1) * limit,
                limit = limit + 1,
                sortBy = sortBy,
                order = order,
                search = search.ifBlank { null }
            )

            hasNextPage = rawUsers.size > limit
            users = rawUsers.take(limit)
        } catch (e: Exception) {
            users = emptyList()
            hasNextPage = false
        }
        isLoading = false
    }

    Column(modifier = Modifier.padding(16.dp)) {
        UserListControls(
            search = search,
            sortBy = sortBy,
            order = order,
            onSearchChange = {
                search = it
                page = 1
            },
            onSortChange = { newSortBy, newOrder ->
                sortBy = newSortBy
                order = newOrder
                page = 1
            }
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (users.isEmpty()) {
                NoUsersFound()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(users) { index, user ->
                        UserCard(
                            index = (page - 1) * limit + index + 1,
                            username = user.username,
                            createdAt = user.createdAt,
                            imageUrl = user.getBackendImageUrl(),
                            followersCount = user.followersCount,
                            uuid = user.uuid,
                            onClick = onUserClick
                        )
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

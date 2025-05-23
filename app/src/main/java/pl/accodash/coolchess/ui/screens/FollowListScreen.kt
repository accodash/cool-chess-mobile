package pl.accodash.coolchess.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.Following
import pl.accodash.coolchess.ui.components.NoUsersFound
import pl.accodash.coolchess.ui.components.UserCard

@Composable
fun FollowListScreen(
    uuid: String,
    isFollowers: Boolean,
    services: CoolChessServices,
    onUserClick: (String) -> Unit
) {
    val service = services.followingService
    var loading by remember { mutableStateOf(true) }
    var followings by remember { mutableStateOf<List<Following>>(emptyList()) }

    LaunchedEffect(uuid, isFollowers) {
        loading = true
        followings = try {
            if (isFollowers) service.fetchFollowers(uuid)
            else service.fetchFollowings(uuid)
        } catch (e: Exception) {
            emptyList()
        }
        loading = false
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (followings.isEmpty()) {
        NoUsersFound()
    } else{
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            itemsIndexed(followings) { index, following ->
                val user = if (isFollowers) following.follower else following.followedUser
                user?.let {
                    UserCard(
                        index = index + 1,
                        username = it.username,
                        createdAt = it.createdAt,
                        imageUrl = it.getBackendImageUrl(),
                        uuid = it.uuid,
                        onClick = onUserClick
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

package pl.accodash.coolchess.ui.screens

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.User
import pl.accodash.coolchess.ui.components.RatingCard
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.models.Following
import pl.accodash.coolchess.api.models.FriendRelation
import pl.accodash.coolchess.ui.components.FollowActionButtons
import pl.accodash.coolchess.ui.components.FriendActionButtons

data class RatingMode(
    val key: String,
    val labelRes: Int,
    val icon: ImageVector,
    val rating: Int
)

@Composable
fun UserProfileScreen(
    uuid: String,
    services: CoolChessServices,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onFollowersClick: (String) -> Unit = {},
    onFollowingClick: (String) -> Unit = {}
) {
    var currentUser by remember { mutableStateOf<User?>(null) }
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    var friends by remember { mutableStateOf<List<FriendRelation>>(emptyList()) }
    var receivedRequests by remember { mutableStateOf<List<FriendRelation>>(emptyList()) }
    var sentRequests by remember { mutableStateOf<List<FriendRelation>>(emptyList()) }
    var followings by remember { mutableStateOf<List<Following>>(emptyList()) }

    LaunchedEffect(uuid) {
        isLoading = true
        isError = false
        try {
            user = services.userService.getUserById(uuid)
        } catch (e: Exception) {
            isError = true
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(user) {
        currentUser = services.userService.getCurrentUser()
        if (user != null && currentUser?.uuid != uuid) {
            friends = services.friendService.fetchFriends()
            receivedRequests = services.friendService.fetchReceivedRequests()
            sentRequests = services.friendService.fetchSentRequests()
            followings = services.followingService.fetchFollowings(currentUser!!.uuid)
        }
    }

    when {
        isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        isError -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.failed_to_load),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        user != null -> {
            val ratingsMap = remember(user) {
                user!!.ratings?.associate { it.mode to it.rating } ?: emptyMap()
            }
            var followersCount by remember {
                mutableIntStateOf(
                    user!!.followersCount ?: user!!.followers?.size ?: 0
                )
            }
            val followingCount by remember { mutableIntStateOf(user!!.followedUsers?.size ?: 0) }

            val ratingModes = listOf(
                RatingMode(
                    "bullet",
                    R.string.bullet,
                    Icons.Default.FlashOn,
                    ratingsMap["bullet"] ?: 400
                ),
                RatingMode(
                    "blitz",
                    R.string.blitz,
                    Icons.Default.Speed,
                    ratingsMap["blitz"] ?: 400
                ),
                RatingMode(
                    "rapid",
                    R.string.rapid,
                    Icons.Default.RocketLaunch,
                    ratingsMap["rapid"] ?: 400
                ),
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (user!!.imageUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user!!.getBackendImageUrl())
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Text(
                        text = user!!.username,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ElevatedButton(
                        onClick = { onFollowersClick(uuid) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("${stringResource(R.string.followers)}: $followersCount")
                    }
                    ElevatedButton(
                        onClick = { onFollowingClick(uuid) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("${stringResource(R.string.followings)}: $followingCount")
                    }
                }

                if (currentUser != null && currentUser!!.uuid == user?.uuid) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(stringResource(R.string.edit_profile))
                    }
                }

                if (currentUser != null && currentUser!!.uuid != user?.uuid) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FollowActionButtons(
                            currentUserId = currentUser!!.uuid,
                            targetUserId = user!!.uuid,
                            followings = followings,
                            followingService = services.followingService,
                            onFollow = { followersCount++ },
                            onUnfollow = { followersCount-- }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FriendActionButtons(
                            currentUserId = currentUser!!.uuid,
                            targetUserId = user!!.uuid,
                            friends = friends,
                            receivedRequests = receivedRequests,
                            sentRequests = sentRequests,
                            friendService = services.friendService
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                ratingModes.forEach { mode ->
                    RatingCard(
                        label = stringResource(mode.labelRes),
                        icon = mode.icon,
                        rating = mode.rating,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

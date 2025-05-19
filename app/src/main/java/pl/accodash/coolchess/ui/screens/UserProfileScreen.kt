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
    showEditButton: Boolean = false,
    onEditProfileClick: () -> Unit = {},
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

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

    when {
        isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        isError -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Failed to load user.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        user != null -> {
            val ratingsMap = remember(user) {
                user!!.ratings?.associate { it.mode to it.rating } ?: emptyMap()
            }
            val followersCount = user!!.followersCount ?: user!!.followers?.size ?: 0
            val followingCount = user!!.followedUsers?.size ?: 0

            val ratingModes = listOf(
                RatingMode("bullet", R.string.bullet, Icons.Default.FlashOn, ratingsMap["bullet"] ?: 400),
                RatingMode("blitz", R.string.blitz, Icons.Default.Speed, ratingsMap["blitz"] ?: 400),
                RatingMode("rapid", R.string.rapid, Icons.Default.RocketLaunch, ratingsMap["rapid"] ?: 400),
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                    Button(onClick = onFollowersClick, modifier = Modifier.weight(1f)) {
                        Text("${stringResource(R.string.followers)}: $followersCount")
                    }
                    Button(onClick = onFollowingClick, modifier = Modifier.weight(1f)) {
                        Text("${stringResource(R.string.following)}: $followingCount")
                    }
                }

                if (showEditButton) {
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

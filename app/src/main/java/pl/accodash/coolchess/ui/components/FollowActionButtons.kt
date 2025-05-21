package pl.accodash.coolchess.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.models.Following
import pl.accodash.coolchess.api.services.FollowingService

@Composable
fun FollowActionButtons(
    currentUserId: String,
    targetUserId: String,
    followings: List<Following>,
    followingService: FollowingService
) {
    val scope = rememberCoroutineScope()
    var isFollowing by remember { mutableStateOf<Boolean>(false) }
    var loading by remember { mutableStateOf(false) }

    if (currentUserId == targetUserId) return

    LaunchedEffect(followings) {
        isFollowing = followings.any { it.followedUser?.uuid == targetUserId }
    }

    if (isFollowing) {
        OutlinedButton(
            onClick = {
                loading = true
                scope.launch {
                    followingService.unfollowUser(targetUserId)
                    isFollowing = false
                    loading = false
                }
            },
            enabled = !loading
        ) {
            Text(stringResource(R.string.unfollow))
        }
    } else {
        Button(
            onClick = {
                loading = true
                scope.launch {
                    followingService.followUser(targetUserId)
                    isFollowing = true
                    loading = false
                }
            },
            enabled = !loading
        ) {
            Text(stringResource(R.string.follow))
        }
    }
}

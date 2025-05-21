package pl.accodash.coolchess.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.models.FriendRelation
import pl.accodash.coolchess.api.services.FriendService

@Composable
fun FriendActionButtons(
    currentUserId: String,
    targetUserId: String,
    friends: List<FriendRelation>,
    receivedRequests: List<FriendRelation>,
    sentRequests: List<FriendRelation>,
    friendService: FriendService
) {
    if (currentUserId == targetUserId) return

    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    var friendRelation by remember { mutableStateOf<FriendRelation?>(null) }
    var receivedRequest by remember { mutableStateOf<FriendRelation?>(null) }
    var sentRequest by remember { mutableStateOf<FriendRelation?>(null) }
    var isFriend by remember { mutableStateOf(false) }

    LaunchedEffect(friends) {
        friendRelation = friends.find {
            it.firstUser.uuid == targetUserId || it.secondUser.uuid == targetUserId
        }
        isFriend = friendRelation?.befriendedAt != null
    }

    LaunchedEffect(receivedRequests) {
        receivedRequest = receivedRequests.find {
            it.firstUser.uuid == targetUserId && it.befriendedAt == null
        }
    }

    LaunchedEffect(sentRequests) {
        sentRequest = sentRequests.find {
            it.secondUser.uuid == targetUserId && it.befriendedAt == null
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        when {
            isFriend -> {
                OutlinedButton(
                    onClick = {
                        loading = true
                        scope.launch {
                            friendService.removeFriend(friendRelation!!.id)
                            friendRelation = null
                            isFriend = false
                            loading = false
                        }
                    },
                    enabled = !loading
                ) {
                    Text(stringResource(R.string.remove_friend))
                }
            }

            receivedRequest != null -> {
                Button(
                    onClick = {
                        loading = true
                        scope.launch {
                            friendService.acceptFriendRequest(receivedRequest!!.id)
                            friendRelation = receivedRequest
                            isFriend = true
                            receivedRequest = null
                            loading = false
                        }
                    },
                    enabled = !loading
                ) {
                    Text(stringResource(R.string.accept_friend_request))
                }
                OutlinedButton(
                    onClick = {
                        loading = true
                        scope.launch {
                            friendService.removeFriend(receivedRequest!!.id)
                            receivedRequest = null
                            loading = false
                        }
                    },
                    enabled = !loading
                ) {
                    Text(stringResource(R.string.reject_friend_request))
                }
            }

            sentRequest != null -> {
                OutlinedButton(
                    onClick = {
                        loading = true
                        scope.launch {
                            friendService.removeFriend(sentRequest!!.id)
                            sentRequest = null
                            loading = false
                        }
                    },
                    enabled = !loading
                ) {
                    Text(stringResource(R.string.cancel_friend_request))
                }
            }

            else -> {
                Button(
                    onClick = {
                        loading = true
                        scope.launch {
                            val newRequest = friendService.sendFriendRequest(targetUserId)
                            sentRequest = newRequest
                            loading = false
                        }
                    },
                    enabled = !loading
                ) {
                    Text(stringResource(R.string.add_friend))
                }
            }
        }
    }
}

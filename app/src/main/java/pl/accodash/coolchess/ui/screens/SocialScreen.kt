package pl.accodash.coolchess.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.FriendRelation
import pl.accodash.coolchess.ui.components.AllUsersList
import pl.accodash.coolchess.ui.components.NoUsersFound
import pl.accodash.coolchess.ui.components.UserCard

enum class SocialTabs(
    @StringRes val label: Int
) {
    Friends(R.string.friends),
    ReceivedFriendRequests(R.string.received_friend_requests),
    SentFriendRequests(R.string.sent_friend_requests),
    AllUsers(R.string.all_users)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    currentUuid: String,
    services: CoolChessServices,
    onUserClick: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    var selectedTab by rememberSaveable { mutableStateOf(SocialTabs.Friends) }
    var timesUpdated by rememberSaveable { mutableIntStateOf(0) }

    var friends by remember { mutableStateOf<List<FriendRelation>>(emptyList()) }
    var receivedRequests by remember { mutableStateOf<List<FriendRelation>>(emptyList()) }
    var sentRequests by remember { mutableStateOf<List<FriendRelation>>(emptyList()) }

    LaunchedEffect(timesUpdated) {
        friends = services.friendService.fetchFriends()
        receivedRequests = services.friendService.fetchReceivedRequests()
        sentRequests = services.friendService.fetchSentRequests()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SecondaryScrollableTabRow(
            selectedTabIndex = selectedTab.ordinal,
            edgePadding = 8.dp
        ) {
            SocialTabs.entries.forEach { item ->
                Tab(
                    selected = selectedTab == item,
                    onClick = { selectedTab = item },
                    text = {
                        Text(
                            text = stringResource(item.label),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            SocialTabs.Friends -> {
                if (friends.isEmpty()) {
                    NoUsersFound()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        itemsIndexed(friends) { index, relation ->
                            val user =
                                if (relation.firstUser.uuid == currentUuid) relation.secondUser else relation.firstUser
                            user.let {
                                UserCard(
                                    index = index + 1,
                                    username = it.username,
                                    createdAt = it.createdAt,
                                    imageUrl = it.getBackendImageUrl(),
                                    uuid = it.uuid,
                                    onClick = onUserClick
                                )
                            }
                        }
                    }
                }
            }

            SocialTabs.ReceivedFriendRequests -> {
                if (receivedRequests.isEmpty()) {
                    NoUsersFound()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        itemsIndexed(receivedRequests) { index, relation ->
                            val user = relation.firstUser
                            user.let {
                                UserCard(
                                    index = index + 1,
                                    username = it.username,
                                    createdAt = it.createdAt,
                                    imageUrl = it.getBackendImageUrl(),
                                    uuid = it.uuid,
                                    onClick = onUserClick,
                                    action = {
                                        Button(onClick = {
                                            scope.launch {
                                                services.friendService.acceptFriendRequest(relation.id)
                                                timesUpdated++
                                            }
                                        }) {
                                            Text(stringResource(R.string.accept_friend_request))
                                        }
                                        OutlinedButton(onClick = {
                                            scope.launch {
                                                services.friendService.removeFriend(relation.id)
                                                timesUpdated++
                                            }
                                        }) {
                                            Text(stringResource(R.string.reject_friend_request))
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            SocialTabs.SentFriendRequests -> {
                if (sentRequests.isEmpty()) {
                    NoUsersFound()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        itemsIndexed(sentRequests) { index, relation ->
                            val user = relation.secondUser
                            user.let {
                                UserCard(
                                    index = index + 1,
                                    username = it.username,
                                    createdAt = it.createdAt,
                                    imageUrl = it.getBackendImageUrl(),
                                    uuid = it.uuid,
                                    onClick = onUserClick,
                                    action = {
                                        OutlinedButton(onClick = {
                                            scope.launch {
                                                services.friendService.removeFriend(relation.id)
                                                timesUpdated++
                                            }
                                        }) {
                                            Text(stringResource(R.string.cancel_friend_request))
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            SocialTabs.AllUsers -> {
                AllUsersList(userService = services.userService, onUserClick = onUserClick)
            }
        }
    }
}

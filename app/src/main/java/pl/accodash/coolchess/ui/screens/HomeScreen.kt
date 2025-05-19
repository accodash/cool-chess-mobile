package pl.accodash.coolchess.ui.screens

import androidx.compose.runtime.*
import pl.accodash.coolchess.api.models.User

@Composable
fun HomeScreen(
    user: User,
    onEditProfileClick: () -> Unit = {},
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {}
) {
    UserProfile(
        user = user,
        showEditButton = true,
        onEditProfileClick = onEditProfileClick,
        onFollowersClick = onFollowersClick,
        onFollowingClick = onFollowingClick
    )
}


package pl.accodash.coolchess.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.models.User

@Composable
fun HomeScreen(
    user: User,
    onEditProfileClick: () -> Unit = {},
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.hello_heading),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        UserProfile(
            user = user,
            showEditButton = true,
            onEditProfileClick = onEditProfileClick,
            onFollowersClick = onFollowersClick,
            onFollowingClick = onFollowingClick
        )
    }
}


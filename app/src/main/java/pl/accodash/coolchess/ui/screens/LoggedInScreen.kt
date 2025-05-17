package pl.accodash.coolchess.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.models.User

enum class BottomNavItem(@StringRes val label: Int, val icon: ImageVector) {
    Home(R.string.home, Icons.Filled.Home),
    History(R.string.history, Icons.Filled.History),
    Ranking(R.string.ranking, Icons.Filled.MilitaryTech),
    Social(R.string.social, Icons.Filled.EmojiPeople),
    More(R.string.more, Icons.Filled.MoreHoriz)
}

@Composable
fun LoggedInScreen(user: User, modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavItem.entries.forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item,
                        onClick = { selectedTab = item },
                        icon = { Icon(item.icon, contentDescription = stringResource(item.label)) },
                        label = { Text(stringResource(item.label)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${selectedTab.label} Screen\nWelcome, ${user.username}!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

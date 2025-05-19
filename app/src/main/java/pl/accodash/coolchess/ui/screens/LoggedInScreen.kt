package pl.accodash.coolchess.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.User

enum class BottomNavItem(@StringRes val label: Int, val icon: ImageVector) {
    Home(R.string.home, Icons.Filled.Home),
    History(R.string.history, Icons.Filled.History),
    Ranking(R.string.ranking, Icons.Filled.MilitaryTech),
    Social(R.string.social, Icons.Filled.EmojiPeople),
    More(R.string.more, Icons.Filled.MoreHoriz)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggedInScreen(
    user: User,
    services: CoolChessServices,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavItem.Home) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(selectedTab.label),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomNavItem.Home -> HomeScreen(user = user)
                BottomNavItem.Ranking -> RankingScreen(services = services)
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${stringResource(selectedTab.label)} Screen\nWelcome, ${user.username}!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}

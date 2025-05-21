package pl.accodash.coolchess.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.User

enum class Screens(
    @StringRes val label: Int,
    val route: String,
    val bottomBarIcon: ImageVector? = null
) {
    Home(R.string.home, "home", Icons.Filled.Home),
    History(R.string.history, "history", Icons.Filled.History),
    Ranking(R.string.ranking, "ranking", Icons.Filled.MilitaryTech),
    Social(R.string.social, "social", Icons.Filled.EmojiPeople),
    More(R.string.more, "more", Icons.Filled.MoreHoriz),
    UserProfile(R.string.user_profile, "user"),
    Followers(R.string.followers, "followers"),
    Followings(R.string.followings, "followings"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggedInScreen(
    user: User,
    services: CoolChessServices,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    var selectedTab by rememberSaveable { mutableStateOf(Screens.Home) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screens.Home.route

    val isBottomNavScreen = Screens.entries.first {
        currentRoute.startsWith(it.route)
    }.bottomBarIcon != null

    val topBarTitle = stringResource(Screens.entries.first {
        currentRoute.startsWith(it.route)
    }.label)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    if (!isBottomNavScreen) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (isBottomNavScreen) {
                NavigationBar {
                    Screens.entries.filter { item -> item.bottomBarIcon != null }
                        .forEach { item ->
                            NavigationBarItem(
                                selected = selectedTab == item,
                                onClick = {
                                    selectedTab = item
                                    navController.navigate(item.name.lowercase()) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        item.bottomBarIcon!!,
                                        contentDescription = stringResource(item.label)
                                    )
                                },
                                label = { Text(stringResource(item.label)) }
                            )
                        }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Home.route) {
                UserProfileScreen(
                    uuid = user.uuid,
                    showEditButton = true,
                    onEditProfileClick = {},
                    onFollowersClick = {navController.navigate("${Screens.Followers.route}/$it")},
                    onFollowingClick = {navController.navigate("${Screens.Followings.route}/$it")},
                    services = services
                )
            }
            composable(Screens.Ranking.route) {
                RankingScreen(
                    services = services,
                    modifier = Modifier.fillMaxSize(),
                    onUserClick = { uuid ->
                        navController.navigate("${Screens.UserProfile.route}/$uuid")
                    }
                )
            }
            composable(Screens.History.route) {

            }
            composable(Screens.Social.route) {

            }
            composable(Screens.More.route) {

            }
            composable(
                "${Screens.UserProfile.route}/{uuid}"
            ) { route ->
                UserProfileScreen(
                    uuid = route.arguments?.getString("uuid") ?: "",
                    services = services,
                    onFollowersClick = {navController.navigate("${Screens.Followers.route}/$it")},
                    onFollowingClick = {navController.navigate("${Screens.Followings.route}/$it")}
                )
            }
            composable("${Screens.Followers.route}/{uuid}") {
                FollowListScreen(
                    uuid = it.arguments?.getString("uuid") ?: "",
                    isFollowers = true,
                    services = services
                ) { uuid ->
                    navController.navigate("user/$uuid")
                }
            }
            composable("${Screens.Followings.route}/{uuid}") {
                FollowListScreen(
                    uuid = it.arguments?.getString("uuid") ?: "",
                    isFollowers = false,
                    services = services
                ) { uuid ->
                    navController.navigate("user/$uuid")
                }
            }
        }
    }
}

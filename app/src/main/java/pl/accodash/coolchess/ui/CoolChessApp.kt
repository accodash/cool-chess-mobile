package pl.accodash.coolchess.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.RetrofitClient
import pl.accodash.coolchess.api.models.User
import pl.accodash.coolchess.api.services.FollowingService
import pl.accodash.coolchess.api.services.FriendService
import pl.accodash.coolchess.api.services.MatchService
import pl.accodash.coolchess.api.services.MoveService
import pl.accodash.coolchess.api.services.RatingService
import pl.accodash.coolchess.api.services.UserService
import pl.accodash.coolchess.auth.AuthManager
import pl.accodash.coolchess.ui.screens.LoadingScreen
import pl.accodash.coolchess.ui.screens.LoggedInScreen
import pl.accodash.coolchess.ui.screens.LoginScreen

fun getServices(token: String) = CoolChessServices(
    userService = RetrofitClient.createService(UserService::class.java, token),
    friendService = RetrofitClient.createService(
        FriendService::class.java,
        token
    ),
    followingService = RetrofitClient.createService(
        FollowingService::class.java,
        token
    ),
    ratingService = RetrofitClient.createService(
        RatingService::class.java,
        token
    ),
    matchService = RetrofitClient.createService(
        MatchService::class.java,
        token
    ),
    moveService = RetrofitClient.createService(
        MoveService::class.java,
        token
    )
)

@Composable
fun CoolChessApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }

    var isLoading by rememberSaveable { mutableStateOf(true) }
    var user by remember { mutableStateOf<User?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var services by remember { mutableStateOf<CoolChessServices?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (authManager.hasSavedCredentials()) {
            try {
                val credentials = authManager.getSavedCredentials()
                if (credentials != null) {
                    val token = credentials.accessToken
                    services = getServices(token)
                    user = services!!.userService.getCurrentUser()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen()
    } else if (user == null) {
        LoginScreen(
            errorMessage = errorMessage,
            onLoginClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        val credentials = authManager.login(context)
                        val token = credentials.accessToken
                        services = getServices(token)
                        user = services!!.userService.getCurrentUser()
                    } catch (e: Exception) {
                        errorMessage = e.message ?: context.getString(R.string.unknown_error)
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = modifier
        )
    } else if (services != null) {
        LoggedInScreen(user = user!!, services = services!!, modifier = modifier, onLogout = {
            scope.launch {
                try {
                    authManager.logout(context)
                    user = null
                } catch (e: Exception) {
                    errorMessage = e.message ?: context.getString(R.string.unknown_error)
                }
            }
        })
    }
}

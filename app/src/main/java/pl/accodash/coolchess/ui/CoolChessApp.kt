package pl.accodash.coolchess.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import pl.accodash.coolchess.api.RetrofitClient
import pl.accodash.coolchess.api.models.User
import pl.accodash.coolchess.api.services.UserService
import pl.accodash.coolchess.auth.AuthManager
import pl.accodash.coolchess.ui.screens.LoadingScreen
import pl.accodash.coolchess.ui.screens.LoggedInScreen
import pl.accodash.coolchess.ui.screens.LoginScreen

@Composable
fun CoolChessApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }

    var isLoading by remember { mutableStateOf(true) }
    var user by remember { mutableStateOf<User?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (authManager.hasSavedCredentials()) {
            try {
                val credentials = authManager.getSavedCredentials()
                if (credentials != null) {
                    val userService = RetrofitClient.createService(UserService::class.java, credentials.accessToken)
                    user = userService.getCurrentUser()
                }
            } catch (e: Exception) {
                errorMessage = "Failed to auto-login: ${e.message}"
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
                        val userService = RetrofitClient.createService(UserService::class.java, credentials.accessToken)
                        user = userService.getCurrentUser()
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Unknown error"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = modifier
        )
    } else {
        LoggedInScreen(user!!, modifier)
    }
}

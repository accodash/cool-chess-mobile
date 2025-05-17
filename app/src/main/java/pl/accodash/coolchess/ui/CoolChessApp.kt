package pl.accodash.coolchess.ui

import androidx.compose.runtime.Composable
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
import pl.accodash.coolchess.ui.screens.LoggedInScreen
import pl.accodash.coolchess.ui.screens.LoginScreen

@Composable
fun CoolChessApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }

    var isLoading by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf<User?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    if (user == null) {
        LoginScreen(
            isLoading = isLoading,
            errorMessage = errorMessage,
            onLoginClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        val credentials = authManager.login(context)
                        val token = credentials.accessToken
                        val userService = RetrofitClient.createService(UserService::class.java, token)
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

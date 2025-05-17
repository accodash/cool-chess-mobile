package pl.accodash.coolchess

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.accodash.coolchess.api.RetrofitClient
import pl.accodash.coolchess.api.models.User
import pl.accodash.coolchess.api.services.UserService
import pl.accodash.coolchess.auth.AuthManager
import pl.accodash.coolchess.ui.theme.CoolChessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoolChessTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }

    var isLoading by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf<User?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (user != null) {
            Text(text = "Hello, ${user!!.username}", style = MaterialTheme.typography.headlineSmall)
        } else {
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            val credentials = authManager.login(context)
                            val token = credentials.accessToken
                            val userService = RetrofitClient.createService(UserService::class.java, token)
                            user = userService.getCurrentUser()
                        } catch (e: Exception) {
                            Log.e("MainScreen", "Auth or Fetch Failed", e)
                            errorMessage = e.message
                        } finally {
                            isLoading = false
                        }
                    }
                }
            ) {
                Text("Login with Auth0")
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
        }
    }
}
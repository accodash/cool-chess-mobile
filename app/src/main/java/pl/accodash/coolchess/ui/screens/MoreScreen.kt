package pl.accodash.coolchess.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R
import pl.accodash.coolchess.ui.components.MoreOption

@Composable
fun MoreScreen(
    onLogout: () -> Unit,
) {
    var isLoggingOut by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MoreOption(
            icon = Icons.AutoMirrored.Filled.Logout,
            label = stringResource(R.string.logout),
            onClick = {
                isLoggingOut = true
                onLogout()
            }
        )
    }

    if (isLoggingOut) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}
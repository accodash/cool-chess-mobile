package pl.accodash.coolchess.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.accodash.coolchess.R
import pl.accodash.coolchess.ui.components.MoreOption
import pl.accodash.coolchess.ui.theme.ThemeColorScheme
import pl.accodash.coolchess.ui.theme.ThemeManager
import pl.accodash.coolchess.ui.theme.ThemeType

@Composable
fun MoreScreen(onLogout: () -> Unit) {
    var isLoggingOut by remember { mutableStateOf(false) }
    var themeDropdownExpanded by remember { mutableStateOf(false) }
    var colorSchemeDropdownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val theme = ThemeManager.currentTheme
    val colorScheme = ThemeManager.currentColorScheme

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box {
            MoreOption(
                icon = Icons.Default.LightMode,
                label = stringResource(R.string.theme),
                caption = theme.displayName,
                onClick = { themeDropdownExpanded = true }
            )

            DropdownMenu(
                expanded = themeDropdownExpanded,
                onDismissRequest = { themeDropdownExpanded = false },
                offset = DpOffset(48.dp, 0.dp)
            ) {
                ThemeType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.displayName) },
                        onClick = {
                            themeDropdownExpanded = false
                            coroutineScope.launch {
                                ThemeManager.setTheme(type)
                                (context as? Activity)?.recreate()
                            }
                        }
                    )
                }
            }
        }

        Box {
            MoreOption(
                icon = Icons.Default.ColorLens,
                label = stringResource(R.string.color_scheme),
                caption = colorScheme.displayName,
                onClick = { colorSchemeDropdownExpanded = true }
            )

            DropdownMenu(
                expanded = colorSchemeDropdownExpanded,
                onDismissRequest = { colorSchemeDropdownExpanded = false },
                offset = DpOffset(48.dp, 0.dp)
            ) {
                ThemeColorScheme.entries.forEach { colorScheme ->
                    DropdownMenuItem(
                        text = { Text(colorScheme.displayName) },
                        onClick = {
                            colorSchemeDropdownExpanded = false
                            coroutineScope.launch {
                                ThemeManager.setColorScheme(colorScheme)
                                (context as? Activity)?.recreate()
                            }
                        }
                    )
                }
            }
        }

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

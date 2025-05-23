package pl.accodash.coolchess.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CoolChessTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColorScheme: ThemeColorScheme,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        themeColorScheme.hasDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> themeColorScheme.darkScheme
        else -> themeColorScheme.lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

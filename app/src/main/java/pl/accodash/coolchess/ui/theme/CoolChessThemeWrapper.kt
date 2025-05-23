package pl.accodash.coolchess.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun CoolChessThemeWrapper(content: @Composable () -> Unit) {
    val colorScheme = ThemeManager.currentColorScheme
    val type = ThemeManager.currentTheme
    val isDark = when (type) {
        ThemeType.Dark -> true
        ThemeType.Light -> false
        ThemeType.System -> isSystemInDarkTheme()
    }

    CoolChessTheme(darkTheme = isDark, themeColorScheme = colorScheme, content = content)
}

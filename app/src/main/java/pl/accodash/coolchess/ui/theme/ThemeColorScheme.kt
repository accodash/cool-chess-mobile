package pl.accodash.coolchess.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme


private val DefaultDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    secondaryContainer = PurpleGrey40,
    tertiary = Pink80
)

private val DefaultLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    secondaryContainer = PurpleGrey80,
    tertiary = Pink40
)

private val RedLimeDarkColorScheme = darkColorScheme(
    primary = Red80,
    secondary = LimeGrey80,
    secondaryContainer = LimeGrey40,
    tertiary = Lime80
)

private val RedLimeLightColorScheme = lightColorScheme(
    primary = Red40,
    secondary = LimeGrey40,
    secondaryContainer = LimeGrey80,
    tertiary = Lime40
)

private val PerfectBlackDarkColorScheme = darkColorScheme(
    primary = White100,
    secondary = Grey100,
    secondaryContainer = Black100,
    tertiary = Grey100,
    background = Black100
)

private val PerfectBlackLightColorScheme = lightColorScheme(
    primary = Black100,
    secondary = Grey100,
    secondaryContainer = White100,
    tertiary = Grey100,
    background = White100
)

enum class ThemeColorScheme(
    val displayName: String,
    val darkScheme: ColorScheme,
    val lightScheme: ColorScheme,
    val hasDynamicColors: Boolean = false
) {
    DynamicScheme("Dynamic colors", DefaultDarkColorScheme, DefaultLightColorScheme, true),
    DefaultScheme("Default purple", DefaultDarkColorScheme, DefaultLightColorScheme),
    RedLimeScheme("Red and lime", RedLimeDarkColorScheme, RedLimeLightColorScheme),
    PerfectBlackScheme("Perfect #000/FFF", PerfectBlackDarkColorScheme, PerfectBlackLightColorScheme),
}

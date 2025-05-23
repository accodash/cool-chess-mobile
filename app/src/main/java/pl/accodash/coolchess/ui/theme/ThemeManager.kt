package pl.accodash.coolchess.ui.theme

import android.content.Context
import pl.accodash.coolchess.database.AppDatabase
import pl.accodash.coolchess.database.repository.UserPreferenceRepository

object ThemeManager {
    private const val THEME_KEY = "theme"
    private const val COLOR_SCHEME_KEY = "color_scheme"

    var currentTheme: ThemeType = ThemeType.System
        private set
    var currentColorScheme: ThemeColorScheme = ThemeColorScheme.DynamicScheme
        private set

    private lateinit var db: AppDatabase

    private lateinit var repo: UserPreferenceRepository

    suspend fun init(context: Context) {
        db = AppDatabase.getInstance(context)
        repo = UserPreferenceRepository(db.userPreferenceDao())

        val storedTheme = repo.getPreference(THEME_KEY)
        val storedColorScheme = repo.getPreference(COLOR_SCHEME_KEY)

        currentTheme = storedTheme?.let { ThemeType.valueOf(it) } ?: ThemeType.System
        currentColorScheme = storedColorScheme?.let { ThemeColorScheme.valueOf(it) } ?: ThemeColorScheme.DynamicScheme
    }

    suspend fun setTheme(type: ThemeType) {
        currentTheme = type
        repo.setPreference(THEME_KEY, type.name)
    }

    suspend fun setColorScheme(colorScheme: ThemeColorScheme) {
        currentColorScheme = colorScheme
        repo.setPreference(COLOR_SCHEME_KEY, colorScheme.name)
    }
}

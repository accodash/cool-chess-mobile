package pl.accodash.coolchess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.accodash.coolchess.ui.CoolChessApp
import pl.accodash.coolchess.ui.theme.CoolChessTheme
import pl.accodash.coolchess.ui.theme.CoolChessThemeWrapper
import pl.accodash.coolchess.ui.theme.ThemeManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            ThemeManager.init(applicationContext)
            runOnUiThread {
                setContent {
                    CoolChessThemeWrapper {
                        CoolChessApp()
                    }
                }
            }
        }
    }
}

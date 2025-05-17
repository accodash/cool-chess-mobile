package pl.accodash.coolchess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import pl.accodash.coolchess.ui.CoolChessApp
import pl.accodash.coolchess.ui.theme.CoolChessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoolChessTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CoolChessApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

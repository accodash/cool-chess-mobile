package pl.accodash.coolchess.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier

private val modes = listOf(
    Triple("bullet", "Bullet", Icons.Default.FlashOn),
    Triple("blitz", "Blitz", Icons.Default.Speed),
    Triple("rapid", "Rapid", Icons.Default.RocketLaunch)
)

@Composable
fun ModeSelector(selectedMode: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 12.dp)) {
        modes.forEach { (mode, label, icon) ->
            FilterChip(
                selected = selectedMode == mode,
                onClick = { onSelect(mode) },
                label = { Text(label) },
                leadingIcon = { Icon(icon, contentDescription = null) }
            )
        }
    }
}

package pl.accodash.coolchess.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListControls(
    search: String,
    sortBy: String,
    order: String,
    onSearchChange: (String) -> Unit,
    onSortChange: (String, String) -> Unit
) {
    val sortOptions = listOf(
        Triple(stringResource(R.string.alphabetically_az), "username", "ASC"),
        Triple(stringResource(R.string.alphabetically_za), "username", "DESC"),
        Triple(stringResource(R.string.created_at_oldest), "createdAt", "ASC"),
        Triple(stringResource(R.string.created_at_newest), "createdAt", "DESC")
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf("$sortBy-$order") }

    val selectedLabel = sortOptions.find { "${it.second}-${it.third}" == selectedOption }?.first ?: ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = onSearchChange,
            label = { Text(stringResource(R.string.search)) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedLabel,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.sort_by)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    sortOptions.forEach { (label, sBy, ord) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedOption = "$sBy-$ord"
                                expanded = false
                                onSortChange(sBy, ord)
                            }
                        )
                    }
                }

        }
    }
}

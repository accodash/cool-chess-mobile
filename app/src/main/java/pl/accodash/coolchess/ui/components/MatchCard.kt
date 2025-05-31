package pl.accodash.coolchess.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.models.Match
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatchCard(
    match: Match,
    currentUserId: String?,
    modifier: Modifier = Modifier,
    isStatic: Boolean = false,
    onClick: (String) -> Unit = { }
) {
    val invalidDateString = stringResource(R.string.invalid)
    val formattedDate = remember(match.startAt) {
        try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = isoFormat.parse(match.startAt)

            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            invalidDateString
        }
    }

    val result = when {
        match.isCompleted && match.winner == null -> stringResource(R.string.draw)
        match.winner?.uuid == currentUserId -> stringResource(R.string.won)
        match.winner?.uuid != null -> stringResource(R.string.lost)
        else -> stringResource(R.string.ongoing)
    }

    val resultColor = when (result) {
        stringResource(R.string.won) -> Color(0xFF359C39)
        stringResource(R.string.lost) -> Color(0xFFCC2518)
        stringResource(R.string.draw) -> Color(0xFF1077C9)
        else -> Color(0xFFD9A404)
    }

    val mode = when (match.mode) {
        "bullet" -> stringResource(R.string.bullet)
        "blitz" -> stringResource(R.string.blitz)
        "rapid" -> stringResource(R.string.rapid)
        else -> match.mode
    }

    val modeIcon = when (match.mode) {
        "bullet" -> Icons.Default.FlashOn
        "blitz" -> Icons.Default.Speed
        "rapid" -> Icons.Default.RocketLaunch
        else -> Icons.Default.Info
    }


    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick(match.id)
            },
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = if (isStatic) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Text(
                formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isStatic) Arrangement.Center else Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painterResource(R.drawable.kw),
                    contentDescription = null,
                    Modifier.height(40.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    match.whitePlayer.username + stringResource(R.string.vs) + match.blackPlayer.username,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(8.dp))
                Image(
                    painterResource(R.drawable.kb),
                    contentDescription = null,
                    Modifier.height(40.dp)
                )
            }

            FlowRow(
                horizontalArrangement = if (isStatic) Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ) else Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(mode) },
                    leadingIcon = { Icon(modeIcon, contentDescription = null) }
                )
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            if (match.isRanked) stringResource(R.string.ranked) else stringResource(
                                R.string.unranked
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            if (match.isRanked) Icons.Default.EmojiEvents else Icons.Default.SentimentVerySatisfied,
                            contentDescription = null
                        )
                    }
                )
                AssistChip(
                    onClick = {},
                    label = { Text(result) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = resultColor)
                )
            }
        }
    }
}

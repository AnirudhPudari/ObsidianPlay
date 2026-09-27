package com.obsidian.shipathon.ui.library

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.ui.theme.ElectricGradient
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingCyan
import com.obsidian.shipathon.ui.theme.GamingGold
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.ObsidianIcons

@Composable
fun GamerStatsCard(
    entries: List<LibraryEntry>,
    modifier: Modifier = Modifier,
) {
    val backlogCount = entries.count { it.status == LibraryStatus.BACKLOG }
    val playingCount = entries.count { it.status == LibraryStatus.PLAYING }
    val completedCount = entries.count { it.status == LibraryStatus.COMPLETED }
    val totalHours = entries.sumOf { it.playtimeHours }

    val totalGames = entries.size
    val completionRate = if (totalGames > 0) (completedCount.toFloat() / totalGames * 100).toInt() else 0

    val gamerLevel = (completedCount * 3 + playingCount * 2 + (totalHours / 10).toInt()).coerceAtLeast(1)
    val gamerTitle = when {
        completedCount >= 15 -> "Master Conqueror"
        completedCount >= 8 -> "Backlog Slayer"
        completedCount >= 3 -> "Dedicated Gamer"
        else -> "Novice Explorer"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = BorderStroke(1.dp, GlassBorderBrush),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Gamer Level Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ElectricGradient),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ObsidianIcons.Trophy,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = GamingGold,
                        )
                    }

                    Column {
                        Text(
                            text = gamerTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = GamingTextPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Gamer Rank: Level $gamerLevel",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingBlue,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GamingStat)
                        .border(1.dp, GlassBorderBrush, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = "$completionRate% Cleared",
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingCyan,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Progress bar with glowing electric color
            LinearProgressIndicator(
                progress = { (completionRate / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = GamingBlue,
                trackColor = GamingStat,
            )

            // 4 Mini Stat Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MiniStatPill(
                    label = "Conquered",
                    value = completedCount.toString(),
                    icon = ObsidianIcons.Trophy,
                    iconTint = GamingGold,
                    modifier = Modifier.weight(1f),
                )
                MiniStatPill(
                    label = "Playing",
                    value = playingCount.toString(),
                    icon = ObsidianIcons.Gamepad,
                    iconTint = GamingBlue,
                    modifier = Modifier.weight(1f),
                )
                MiniStatPill(
                    label = "Backlog",
                    value = backlogCount.toString(),
                    icon = ObsidianIcons.Home,
                    iconTint = GamingCyan,
                    modifier = Modifier.weight(1f),
                )
                MiniStatPill(
                    label = "Logged",
                    value = "${totalHours.toInt()}h",
                    icon = ObsidianIcons.Search,
                    iconTint = GamingTextSecondary,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun MiniStatPill(
    label: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GamingStat)
            .border(1.dp, GlassBorderBrush, RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = iconTint,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = GamingTextPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = GamingTextSecondary,
            fontSize = 9.sp,
        )
    }
}

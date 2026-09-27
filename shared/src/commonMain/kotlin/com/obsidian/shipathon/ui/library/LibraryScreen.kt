package com.obsidian.shipathon.ui.library

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.domain.library.displayName
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.ui.theme.ElectricGradient
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBackgroundNebula
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingCyan
import com.obsidian.shipathon.ui.theme.GamingEmerald
import com.obsidian.shipathon.ui.theme.GamingGold
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.ObsidianIcons

@Composable
fun LibraryScreen(
    libraryGames: List<LibraryEntry> = emptyList(),
    onGameClick: (Game) -> Unit = {},
    onStatusChange: (Game, LibraryStatus) -> Unit = { _, _ -> },
    onOpenRoulette: () -> Unit = {},
    onOpenShareCard: (Game) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
) {
    val tabs = LibraryStatus.entries
    var selectedTabIndex by remember { mutableStateOf(0) }

    val backlogGames = libraryGames.filter { it.status == LibraryStatus.BACKLOG }
    val playingGames = libraryGames.filter { it.status == LibraryStatus.PLAYING }
    val completedGames = libraryGames.filter { it.status == LibraryStatus.COMPLETED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingBackgroundNebula),
    ) {
        // ── Tab row ─────────────────────────────────────────────────────────
        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = GamingBackground,
            contentColor = GamingBlue,
            divider = {},
        ) {
            tabs.forEachIndexed { index, status ->
                val count = when (status) {
                    LibraryStatus.BACKLOG -> backlogGames.size
                    LibraryStatus.PLAYING -> playingGames.size
                    LibraryStatus.COMPLETED -> completedGames.size
                }
                Tab(
                    text = {
                        Text(
                            "${status.displayName()} ($count)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) GamingBlue else GamingTextSecondary,
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }

        // ── Tab content ─────────────────────────────────────────────────────
        when (selectedTabIndex) {
            0 -> BacklogTabContent(
                games = backlogGames,
                onGameClick = onGameClick,
                onStartPlaying = { onStatusChange(it.toGame(), LibraryStatus.PLAYING) },
                onOpenRoulette = onOpenRoulette,
                onNavigateToSearch = onNavigateToSearch,
            )

            1 -> PlayingTabContent(
                games = playingGames,
                onGameClick = onGameClick,
                onMarkCompleted = { onStatusChange(it.toGame(), LibraryStatus.COMPLETED) },
                onNavigateToSearch = onNavigateToSearch,
            )

            2 -> CompletedTabContent(
                games = completedGames,
                onGameClick = onGameClick,
                onShareTrophy = { onOpenShareCard(it.toGame()) },
                onNavigateToSearch = onNavigateToSearch,
            )
        }
    }
}

@Composable
private fun BacklogTabContent(
    games: List<LibraryEntry>,
    onGameClick: (Game) -> Unit,
    onStartPlaying: (LibraryEntry) -> Unit,
    onOpenRoulette: () -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Backlog Roulette Trigger Banner
        if (games.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(GamingCard)
                    .border(1.dp, GlassBorderBrush, RoundedCornerShape(14.dp))
                    .clickable(onClick = onOpenRoulette)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = ObsidianIcons.Dice,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = GamingCyan,
                    )
                    Text(
                        text = "Can't decide? Spin Backlog Roulette",
                        style = MaterialTheme.typography.labelMedium,
                        color = GamingTextPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text("Spin →", color = GamingBlue, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }

        if (games.isEmpty()) {
            EmptyLibraryState(
                title = "Your Backlog is Empty",
                message = "Add games from Home, Search, or share videos from TikTok & Instagram!",
                actionLabel = "Find Games to Add",
                onAction = onNavigateToSearch,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(games) { entry ->
                    LibraryGameCard(
                        libraryGame = entry,
                        onClick = { onGameClick(entry.toGame()) },
                        actionButton = {
                            Button(
                                onClick = { onStartPlaying(entry) },
                                colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            ) {
                                Text("Play", color = GamingTextPrimary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayingTabContent(
    games: List<LibraryEntry>,
    onGameClick: (Game) -> Unit,
    onMarkCompleted: (LibraryEntry) -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    if (games.isEmpty()) {
        EmptyLibraryState(
            title = "No Games Currently in Progress",
            message = "Move a game from your Backlog to Playing to start tracking your quest!",
            actionLabel = "Browse Games",
            onAction = onNavigateToSearch,
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(games) { entry ->
                LibraryGameCard(
                    libraryGame = entry,
                    onClick = { onGameClick(entry.toGame()) },
                    actionButton = {
                        Button(
                            onClick = { onMarkCompleted(entry) },
                            colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    imageVector = ObsidianIcons.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = GamingTextPrimary,
                                )
                                Text(
                                    text = "Finish",
                                    color = GamingTextPrimary,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun CompletedTabContent(
    games: List<LibraryEntry>,
    onGameClick: (Game) -> Unit,
    onShareTrophy: (LibraryEntry) -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    if (games.isEmpty()) {
        EmptyLibraryState(
            title = "No Games Conquered Yet",
            message = "Complete campaigns to build your Gaming Trophy Room!",
            actionLabel = "Explore Backlog",
            onAction = onNavigateToSearch,
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(games) { entry ->
                LibraryGameCard(
                    libraryGame = entry,
                    onClick = { onGameClick(entry.toGame()) },
                    actionButton = {
                        Button(
                            onClick = { onShareTrophy(entry) },
                            colors = ButtonDefaults.buttonColors(containerColor = GamingCard),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, GlassBorderBrush),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    imageVector = ObsidianIcons.Trophy,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = GamingGold,
                                )
                                Text("Trophy", color = GamingGold, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun EmptyLibraryState(
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingBackground)
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(GamingCard)
                    .border(1.dp, GlassBorderBrush, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ObsidianIcons.Gamepad,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = GamingBlue,
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = GamingTextPrimary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = GamingTextSecondary,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(actionLabel, color = GamingTextPrimary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LibraryGameCard(
    libraryGame: LibraryEntry,
    onClick: () -> Unit,
    actionButton: @Composable () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = BorderStroke(1.dp, GlassBorderBrush),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = libraryGame.coverUrl,
                contentDescription = libraryGame.name,
                modifier = Modifier
                    .width(60.dp)
                    .height(84.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = libraryGame.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = GamingTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                if (libraryGame.genres.isNotEmpty()) {
                    Text(
                        text = libraryGame.genres.take(2).joinToString(" · "),
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                if (libraryGame.status == LibraryStatus.PLAYING) {
                    Spacer(Modifier.height(2.dp))
                    LinearProgressIndicator(
                        progress = { libraryGame.progressPercent.coerceIn(0, 100) / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = GamingBlue,
                        trackColor = GamingStat,
                    )
                    Text(
                        text = "${libraryGame.progressPercent}% • ${libraryGame.playtimeHours.toInt()}h logged",
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingTextSecondary,
                        fontSize = 10.sp,
                    )
                }

                if (libraryGame.status == LibraryStatus.COMPLETED) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 2.dp),
                    ) {
                        Icon(
                            imageVector = ObsidianIcons.Trophy,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = GamingGold,
                        )
                        val ratingSnippet = libraryGame.rating?.let { r ->
                            if (r <= 5.0) "★ ${r.toInt()}/5 • " else "★ ${r.toInt()}% • "
                        } ?: ""
                        Text(
                            text = "${ratingSnippet}CONQUERED • ${libraryGame.playtimeHours.toInt()}h",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))
            actionButton()
        }
    }
}

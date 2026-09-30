package com.obsidian.shipathon.ui.share

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.obsidian.shipathon.di.AppContainer
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.domain.share.ShareSourceType
import com.obsidian.shipathon.domain.share.SocialShareParser
import com.obsidian.shipathon.ui.theme.DialogBorderGlowBrush
import com.obsidian.shipathon.ui.theme.DialogSurfaceGradient
import com.obsidian.shipathon.ui.theme.ElectricGradient
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingCyan
import com.obsidian.shipathon.ui.theme.GamingEmerald
import com.obsidian.shipathon.ui.theme.GamingGold
import com.obsidian.shipathon.ui.theme.GamingRed
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.InstagramGradient
import com.obsidian.shipathon.ui.theme.ObsidianIcons
import com.obsidian.shipathon.ui.theme.SteamGradient
import com.obsidian.shipathon.ui.theme.TikTokGradient
import com.obsidian.shipathon.ui.theme.YouTubeGradient

@Composable
fun QuickShareImportDialog(
    rawSharedText: String,
    libraryGames: List<LibraryEntry> = emptyList(),
    onDismiss: () -> Unit,
    onSaveGame: (Game, LibraryStatus) -> Unit,
    onGameClick: (Game) -> Unit = {},
) {
    val parseResult = remember(rawSharedText) {
        SocialShareParser.parse(rawSharedText)
    }

    var searchQuery by remember(rawSharedText) { mutableStateOf(parseResult.extractedQuery) }
    var searchResults by remember { mutableStateOf<List<Game>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(rawSharedText) {
        if (searchQuery.isBlank() && rawSharedText.contains("http", ignoreCase = true)) {
            isSearching = true
            val resolvedTitle = SocialShareParser.resolveVideoTitle(rawSharedText)
            if (!resolvedTitle.isNullOrBlank()) {
                val parsed = SocialShareParser.parse(resolvedTitle)
                searchQuery = if (parsed.extractedQuery.isNotBlank()) parsed.extractedQuery else resolvedTitle
            } else {
                isSearching = false
            }
        }
    }

    LaunchedEffect(searchQuery) {
        val trimmed = searchQuery.trim()
        if (trimmed.isNotBlank()) {
            isSearching = true
            searchError = null
            val primaryResult = AppContainer.gameRepository.searchGames(trimmed, limit = 10)
            primaryResult.onSuccess { games ->
                if (games.isNotEmpty()) {
                    searchResults = games
                    isSearching = false
                } else {
                    // Progressive Fallback: If full multi-word title returned 0 matches, search first 2-3 words
                    val words = trimmed.split(Regex("\\s+"))
                    if (words.size > 2) {
                        val fallbackQuery = words.take(2).joinToString(" ")
                        val fallbackResult = AppContainer.gameRepository.searchGames(fallbackQuery, limit = 10)
                        fallbackResult.onSuccess { fallbackGames ->
                            searchResults = fallbackGames
                            if (fallbackGames.isNotEmpty()) {
                                searchQuery = fallbackQuery
                            }
                            isSearching = false
                        }.onFailure {
                            searchResults = emptyList()
                            isSearching = false
                        }
                    } else {
                        searchResults = emptyList()
                        isSearching = false
                    }
                }
            }.onFailure { err ->
                searchError = err.message ?: "Failed to search games"
                isSearching = false
            }
        } else {
            searchResults = emptyList()
            isSearching = false
        }
    }

    val sourceBrush = when (parseResult.sourceType) {
        ShareSourceType.TIKTOK    -> TikTokGradient
        ShareSourceType.INSTAGRAM -> InstagramGradient
        ShareSourceType.YOUTUBE   -> YouTubeGradient
        ShareSourceType.TWITCH    -> com.obsidian.shipathon.ui.theme.TwitchGradient
        ShareSourceType.REDDIT    -> com.obsidian.shipathon.ui.theme.RedditGradient
        ShareSourceType.TWITTER   -> com.obsidian.shipathon.ui.theme.TwitterGradient
        ShareSourceType.STEAM     -> SteamGradient
        else                      -> ElectricGradient
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Color(0x24FFFFFF), RoundedCornerShape(24.dp)),
            color = GamingBackground,
            shadowElevation = 24.dp,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 36.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(sourceBrush),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Link,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = GamingTextPrimary,
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Text(
                                    text = parseResult.sourceType.displayName,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = GamingTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = parseResult.sourceType.categoryName,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = GamingCyan,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Text(
                                text = if (parseResult.sourceType == ShareSourceType.MANUAL_INPUT)
                                    "Paste TikTok, Reels, Shorts or Steam links"
                                else
                                    "1-tap add to your gaming bucket list",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingTextSecondary,
                                fontSize = 11.sp,
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Editable Game Title Search Input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Game title...", color = GamingTextSecondary, fontSize = 13.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = GamingCard,
                            unfocusedContainerColor = GamingCard,
                            focusedBorderColor = GamingBlue,
                            unfocusedBorderColor = GamingStat,
                            focusedTextColor = GamingTextPrimary,
                            unfocusedTextColor = GamingTextPrimary,
                        ),
                    )

                    Spacer(Modifier.height(12.dp))

                    // Search Results Area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp, max = 300.dp),
                    ) {
                        when {
                            isSearching -> {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    CircularProgressIndicator(color = GamingBlue, modifier = Modifier.size(28.dp), strokeWidth = 2.5.dp)
                                    Spacer(Modifier.height(8.dp))
                                    Text("Searching games...", color = GamingTextSecondary, style = MaterialTheme.typography.labelSmall)
                                }
                            }

                            searchError != null -> {
                                Text(
                                    text = "Could not find matches for \"$searchQuery\"",
                                    color = GamingRed,
                                    modifier = Modifier.align(Alignment.Center),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }

                            searchResults.isEmpty() -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(GamingStat),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = if (searchQuery.isNotBlank()) ObsidianIcons.Search else ObsidianIcons.Link,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp),
                                            tint = GamingCyan,
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Text(
                                            text = if (searchQuery.isNotBlank())
                                                "No direct match for \"${searchQuery.take(24)}\""
                                            else
                                                "Link received from ${parseResult.sourceType.displayName}",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = GamingTextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                        )
                                        Text(
                                            text = if (searchQuery.isNotBlank())
                                                "Mini-games and playables may not be in IGDB. Search a title or pick below:"
                                            else
                                                "Type a game name above or tap a trending title to add:",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = GamingTextSecondary,
                                            textAlign = TextAlign.Center,
                                            fontSize = 11.sp,
                                        )
                                    }

                                    // Quick Suggestion Chips
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        val suggestions = listOf("Elden Ring", "Cyberpunk 2077", "GTA V", "Wukong")
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        ) {
                                            suggestions.forEach { suggestion ->
                                                Surface(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .clickable { searchQuery = suggestion },
                                                    color = GamingCard,
                                                    border = BorderStroke(1.dp, Color(0x22FFFFFF)),
                                                    shape = RoundedCornerShape(8.dp),
                                                ) {
                                                    Text(
                                                        text = suggestion,
                                                        color = GamingCyan,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    items(searchResults) { game ->
                                        val existingEntry = libraryGames.firstOrNull { it.gameId == game.id }
                                        QuickShareGameResultCard(
                                            game = game,
                                            existingStatus = existingEntry?.status,
                                            onAddToBacklog = {
                                                onSaveGame(game, LibraryStatus.BACKLOG)
                                                onDismiss()
                                            },
                                            onStartPlaying = {
                                                onSaveGame(game, LibraryStatus.PLAYING)
                                                onDismiss()
                                            },
                                            onClick = { onGameClick(game) },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Sleek Close Button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(10f)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(GamingStat)
                        .border(1.dp, Color(0x18FFFFFF), CircleShape)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = ObsidianIcons.Close,
                        contentDescription = "Dismiss",
                        modifier = Modifier.size(14.dp),
                        tint = GamingTextSecondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickShareGameResultCard(
    game: Game,
    existingStatus: LibraryStatus?,
    onAddToBacklog: () -> Unit,
    onStartPlaying: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = BorderStroke(1.dp, Color(0x18FFFFFF)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = game.coverUrl,
                contentDescription = game.name,
                modifier = Modifier
                    .size(width = 44.dp, height = 60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = GamingTextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (game.genres.isNotEmpty()) {
                    Text(
                        text = game.genres.take(2).joinToString(" · "),
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1,
                    )
                }
                game.rating?.let { rating ->
                    val starScore = ((if (rating <= 5.0) rating else rating / 20.0) * 10).toInt() / 10.0
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                    ) {
                        Icon(
                            imageVector = ObsidianIcons.Star,
                            contentDescription = null,
                            modifier = Modifier.size(11.dp),
                            tint = GamingGold,
                        )
                        Text(
                            text = "$starScore",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            if (existingStatus != null) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF064E3B).copy(alpha = 0.8f))
                        .border(1.dp, GamingEmerald.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector = ObsidianIcons.Check,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = GamingEmerald,
                    )
                    Text(
                        text = existingStatus.name,
                        color = GamingEmerald,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                    )
                }
            } else {
                Button(
                    onClick = onAddToBacklog,
                    colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Text(
                        text = "+ Backlog",
                        color = GamingTextPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

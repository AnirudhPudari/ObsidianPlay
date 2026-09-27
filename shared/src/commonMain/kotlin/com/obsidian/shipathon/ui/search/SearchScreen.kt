package com.obsidian.shipathon.ui.search

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.obsidian.shipathon.di.AppContainer
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.ui.components.CyberpunkOfflineState
import com.obsidian.shipathon.ui.components.ShimmerBox
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBackgroundNebula
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
import com.obsidian.shipathon.ui.theme.ObsidianIcons
import kotlin.time.Instant
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val POPULAR_SEARCH_TAGS = listOf(
    "Elden Ring", "Cyberpunk", "Zelda", "Hades", "Silent Hill", "Monster Hunter", "GTA", "Persona", "Final Fantasy"
)

@Composable
fun SearchScreen(
    libraryGames: List<LibraryEntry> = emptyList(),
    onToggleBacklog: (Game) -> Unit = {},
    onGameClick: (Game) -> Unit = {},
    onOpenSocialPasteDialog: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Game>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.trim().length >= 2) {
            delay(350)
            isSearching = true
            errorMessage = null
            AppContainer.gameRepository.searchGames(searchQuery.trim(), limit = 20)
                .onSuccess {
                    searchResults = it
                    isSearching = false
                }
                .onFailure {
                    errorMessage = it.message ?: "Failed to search games"
                    isSearching = false
                }
        } else if (searchQuery.isBlank()) {
            searchResults = emptyList()
            isSearching = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingBackgroundNebula),
    ) {
        // Search Input Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Search Input Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = ObsidianIcons.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = GamingTextSecondary,
                    )
                },
                placeholder = {
                    Text("Search 100,000+ games across all platforms...", color = GamingTextSecondary, fontSize = 14.sp)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = ObsidianIcons.Close,
                            contentDescription = "Clear search",
                            tint = GamingTextSecondary,
                            modifier = Modifier
                                .clickable { searchQuery = "" }
                                .padding(8.dp)
                                .size(16.dp),
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = GamingCard,
                    unfocusedContainerColor = GamingCard,
                    focusedBorderColor = GamingBlue,
                    unfocusedBorderColor = GamingStat,
                    focusedTextColor = GamingTextPrimary,
                    unfocusedTextColor = GamingTextPrimary,
                ),
            )

            // Social Share Quick Import Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(onClick = onOpenSocialPasteDialog),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCard),
                border = BorderStroke(1.dp, Color(0x18FFFFFF)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingBlue.copy(alpha = 0.15f))
                            .border(1.dp, GamingBlue.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ObsidianIcons.Link,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = GamingBlue,
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Paste Social Video Link",
                            style = MaterialTheme.typography.bodySmall,
                            color = GamingTextPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                        )
                        Text(
                            text = "TikTok, Reels, Shorts & Steam",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingCyan,
                            fontSize = 11.sp,
                            maxLines = 1,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GamingBlue)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Import",
                            color = GamingTextPrimary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            // Quick Popular Suggestion Tags
            if (searchQuery.isBlank()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = 4.dp),
                ) {
                    items(POPULAR_SEARCH_TAGS) { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(GamingStat)
                                .border(1.dp, GlassBorderBrush, RoundedCornerShape(10.dp))
                                .clickable { searchQuery = tag }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingTextSecondary,
                            )
                        }
                    }
                }
            }
        }

        // Search Results / Empty / Loading Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            when {
                isSearching -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        repeat(5) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = GamingCard),
                                border = BorderStroke(1.dp, GlassBorderBrush),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    ShimmerBox(modifier = Modifier.size(68.dp, 88.dp), cornerRadius = 12.dp)
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        ShimmerBox(modifier = Modifier.width(160.dp).height(18.dp), cornerRadius = 4.dp)
                                        ShimmerBox(modifier = Modifier.width(100.dp).height(12.dp), cornerRadius = 4.dp)
                                        ShimmerBox(modifier = Modifier.width(70.dp).height(12.dp), cornerRadius = 4.dp)
                                    }
                                }
                            }
                        }
                    }
                }

                errorMessage != null -> {
                    CyberpunkOfflineState(
                        onRetry = {
                            // trigger search retry
                            val q = searchQuery
                            searchQuery = ""
                            searchQuery = q
                        },
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                searchQuery.isNotBlank() && searchResults.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = ObsidianIcons.Search,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            tint = GamingTextSecondary,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "No games found for \"$searchQuery\"",
                            color = GamingTextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Try checking for typos or searching alternative titles",
                            color = GamingTextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                searchQuery.isBlank() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(GamingCard)
                                .border(1.dp, GlassBorderBrush, RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Gamepad,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = GamingBlue,
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Explore & Add to Your Bucket List",
                            color = GamingTextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Search by game title, franchise, or paste a social video link above",
                            color = GamingTextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
                    ) {
                        items(searchResults) { game ->
                            val libraryEntry = libraryGames.firstOrNull { it.gameId == game.id }
                            SearchResultGameCard(
                                game = game,
                                libraryStatus = libraryEntry?.status,
                                onToggleBacklog = { onToggleBacklog(game) },
                                onClick = { onGameClick(game) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultGameCard(
    game: Game,
    libraryStatus: LibraryStatus?,
    onToggleBacklog: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = BorderStroke(1.dp, GlassBorderBrush),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = game.coverUrl,
                contentDescription = game.name,
                modifier = Modifier
                    .size(width = 65.dp, height = 90.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = GamingTextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    game.firstReleaseDate?.let { ts ->
                        val year = Instant.fromEpochSeconds(ts)
                            .toLocalDateTime(TimeZone.UTC).year
                        Text(
                            text = year.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingTextSecondary,
                        )
                    }

                    game.rating?.let { rating ->
                        val starScore = ((if (rating <= 5.0) rating else rating / 20.0) * 10).toInt() / 10.0
                        Text(
                            text = "★ $starScore",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingGold,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                if (game.genres.isNotEmpty()) {
                    Text(
                        text = game.genres.take(2).joinToString(" · "),
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            val isInLibrary = libraryStatus != null
            Button(
                onClick = onToggleBacklog,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInLibrary) Color(0xFF059669) else GamingBlue,
                ),
                shape = RoundedCornerShape(10.dp),
                border = if (isInLibrary) BorderStroke(1.dp, Color(0xFF34D399)) else null,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
            ) {
                Text(
                    text = when (libraryStatus) {
                        LibraryStatus.BACKLOG -> "✓ Backlog"
                        LibraryStatus.PLAYING -> "▶ Playing"
                        LibraryStatus.COMPLETED -> "★ Done"
                        null -> "+ Backlog"
                    },
                    color = GamingTextPrimary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

package com.obsidian.shipathon.ui.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.ui.components.CyberpunkOfflineState
import com.obsidian.shipathon.ui.components.HomeSkeletonFeed
import com.obsidian.shipathon.ui.theme.CardBackdropOverlay
import com.obsidian.shipathon.ui.theme.CyanGradient
import com.obsidian.shipathon.ui.theme.ElectricGradient
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBackgroundNebula
import com.obsidian.shipathon.ui.theme.HeroAmbientGlow
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
import com.obsidian.shipathon.ui.theme.GoldGradient
import com.obsidian.shipathon.ui.theme.ObsidianIcons
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val GENRE_FILTERS = listOf("All", "RPG", "Action", "Horror", "Adventure", "Shooter", "Indie", "Strategy")

/** Helper to cleanly format platform names so long strings like "PC (Microsoft Windows)" never overflow */
fun formatPlatformName(platform: String): String = when {
    platform.contains("PC", ignoreCase = true) || platform.contains("Windows", ignoreCase = true) -> "PC"
    platform.contains("PlayStation 5", ignoreCase = true) || platform.contains("PS5", ignoreCase = true) -> "PS5"
    platform.contains("PlayStation 4", ignoreCase = true) || platform.contains("PS4", ignoreCase = true) -> "PS4"
    platform.contains("PlayStation", ignoreCase = true) -> "PlayStation"
    platform.contains("Series X", ignoreCase = true) || platform.contains("Series S", ignoreCase = true) -> "XSX"
    platform.contains("Xbox One", ignoreCase = true) -> "XONE"
    platform.contains("Xbox", ignoreCase = true) -> "Xbox"
    platform.contains("Switch", ignoreCase = true) || platform.contains("Nintendo", ignoreCase = true) -> "Switch"
    platform.contains("Mac", ignoreCase = true) || platform.contains("Apple", ignoreCase = true) -> "Mac"
    platform.contains("Android", ignoreCase = true) -> "Android"
    platform.contains("Linux", ignoreCase = true) -> "Linux"
    else -> platform.take(8)
}

/** Helper to convert 0-100 rating to standard intuitive 5-star rating (e.g. 98 -> "4.9") */
fun toStarScore(rating: Double?): String {
    if (rating == null || rating <= 0.0) return "4.5"
    val score = if (rating <= 5.0) rating else (rating / 20.0)
    val rounded = (score * 10).toInt() / 10.0
    return rounded.toString()
}

@Composable
fun HomeScreen(
    popularGames: List<Game>,
    newReleases: List<Game>,
    libraryGames: List<LibraryEntry> = emptyList(),
    errorMessage: String?,
    isLoading: Boolean,
    onRetry: () -> Unit = {},
    onToggleBacklog: (Game) -> Unit = {},
    onGameClick: (Game) -> Unit = {},
    onNavigateToSearch: (String) -> Unit = {},
) {
    var selectedGenre by remember { mutableStateOf("All") }

    when {
        isLoading -> HomeSkeletonFeed()

        errorMessage != null && popularGames.isEmpty() -> CyberpunkOfflineState(onRetry = onRetry)

        popularGames.isEmpty() -> Box(
            modifier = Modifier.fillMaxSize().background(GamingBackground),
        ) {
            Text(
                text = "No games found.",
                modifier = Modifier.align(Alignment.Center),
                color = GamingTextSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        else -> {
            val libraryGameIds = remember(libraryGames) { libraryGames.map { it.gameId }.toSet() }

            // Smart Discovery Queue: Pick the top popular game that the user has NOT saved yet
            val featuredGame = popularGames.firstOrNull { it.id !in libraryGameIds } ?: popularGames.first()
            val isFeaturedSaved = featuredGame.id in libraryGameIds
            val libraryStatus = libraryGames.firstOrNull { it.gameId == featuredGame.id }?.status

            // Multi-genre fuzzy matching
            fun matchesGenre(game: Game, genre: String): Boolean {
                if (genre == "All") return true
                return game.genres.any { g ->
                    when (genre) {
                        "RPG"       -> g.contains("Role-playing", ignoreCase = true) || g.contains("RPG", ignoreCase = true)
                        "Action"    -> g.contains("Action", ignoreCase = true) || g.contains("Shooter", ignoreCase = true) || g.contains("Hack", ignoreCase = true) || g.contains("Fighting", ignoreCase = true)
                        "Horror"    -> g.contains("Horror", ignoreCase = true) || g.contains("Survival", ignoreCase = true) || g.contains("Thriller", ignoreCase = true)
                        "Adventure" -> g.contains("Adventure", ignoreCase = true) || g.contains("Open", ignoreCase = true)
                        "Strategy"  -> g.contains("Strategy", ignoreCase = true) || g.contains("Tactical", ignoreCase = true) || g.contains("Turn-based", ignoreCase = true)
                        "Indie"     -> g.contains("Indie", ignoreCase = true) || g.contains("Arcade", ignoreCase = true) || g.contains("Platform", ignoreCase = true)
                        else        -> g.contains(genre, ignoreCase = true)
                    }
                }
            }

            val filteredPopular = popularGames.filter { matchesGenre(it, selectedGenre) }
            val filteredNew = newReleases.filter { matchesGenre(it, selectedGenre) }
            val topRatedGames = popularGames.filter { (it.rating ?: 0.0) >= 80.0 && matchesGenre(it, selectedGenre) }

            val hasGenreMatches = filteredPopular.isNotEmpty() || filteredNew.isNotEmpty()

            LazyColumn(
                modifier = Modifier.fillMaxSize().background(GamingBackgroundNebula),
                contentPadding = PaddingValues(top = 4.dp, bottom = 36.dp),
            ) {
                // ── 1. Floating Console Hero Spotlight (Auto-advancing discovery queue) ──
                item {
                    FeaturedSection(
                        game = featuredGame,
                        isSaved = isFeaturedSaved,
                        libraryStatus = libraryStatus,
                        onAddToBacklog = { onToggleBacklog(featuredGame) },
                        onGameClick = { onGameClick(featuredGame) },
                    )
                }

                // ── 2. Genre Filter Chips ─────────────────────────────────────
                item {
                    GenreFilterRow(
                        selectedGenre = selectedGenre,
                        onSelectGenre = { selectedGenre = it },
                    )
                }

                // ── 3. Genre Empty State ──────────────────────────────────────
                if (!hasGenreMatches && selectedGenre != "All") {
                    item {
                        GenreEmptyState(
                            genre = selectedGenre,
                            onSearchOnline = { onNavigateToSearch(selectedGenre) },
                        )
                    }
                }

                // ── 4. Trending Section ───────────────────────────────────────
                if (filteredPopular.isNotEmpty()) {
                    item {
                        ModernSectionHeader(
                            title = "Trending Now",
                            badge = "HOT",
                            accentGradient = ElectricGradient,
                            count = filteredPopular.size,
                        )
                    }
                    item {
                        HorizontalGameList(
                            games = filteredPopular,
                            libraryGames = libraryGames,
                            onGameClick = onGameClick,
                            onQuickAdd = onToggleBacklog,
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // ── 5. Top Rated Masterpieces ─────────────────────────────────
                if (topRatedGames.isNotEmpty()) {
                    item {
                        ModernSectionHeader(
                            title = "Critic Acclaimed",
                            badge = "TOP RATED",
                            accentGradient = GoldGradient,
                            count = topRatedGames.size,
                        )
                    }
                    item {
                        HorizontalGameList(
                            games = topRatedGames,
                            libraryGames = libraryGames,
                            onGameClick = onGameClick,
                            onQuickAdd = onToggleBacklog,
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // ── 6. Fresh Drops & New Releases ─────────────────────────────
                if (filteredNew.isNotEmpty()) {
                    item {
                        ModernSectionHeader(
                            title = "Fresh Drops",
                            badge = "NEW",
                            accentGradient = CyanGradient,
                            count = filteredNew.size,
                        )
                    }
                    item {
                        HorizontalGameList(
                            games = filteredNew,
                            libraryGames = libraryGames,
                            onGameClick = onGameClick,
                            onQuickAdd = onToggleBacklog,
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

// ── 1. Floating Console Hero Spotlight ────────────────────────────────────────

@Composable
private fun FeaturedSection(
    game: Game,
    isSaved: Boolean,
    libraryStatus: LibraryStatus?,
    onAddToBacklog: () -> Unit,
    onGameClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        // Atmospheric Ambient Halo Glow behind Hero Spotlight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(HeroAmbientGlow)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .clickable(onClick = onGameClick),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GamingCard),
            border = BorderStroke(1.2.dp, GlassBorderBrush),
            elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
        ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp),
            ) {
                AsyncImage(
                    model = game.coverUrl,
                    contentDescription = game.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CardBackdropOverlay),
                )

                // Top Header: Spotlight Tag & Clear Star Rating Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GamingBackground.copy(alpha = 0.88f))
                            .border(1.dp, GlassBorderBrush, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isSaved) GamingBlue else GamingRed)
                        )
                        Text(
                            text = if (isSaved) "IN YOUR BUCKET LIST" else "FEATURED DISCOVERY",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                            color = if (isSaved) GamingBlue else GamingRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                        )
                    }

                    game.rating?.let { rating ->
                        val starScore = toStarScore(rating)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GamingBackground.copy(alpha = 0.88f))
                                .border(1.dp, GamingGold.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                        ) {
                            Text(
                                text = "★ $starScore",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingGold,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                // Bottom Content pinned inside artwork area
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    Text(
                        text = game.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = GamingTextPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(6.dp))

                    // Meta Row: Year · Genres · Platforms
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        game.firstReleaseDate?.let { ts ->
                            val year = Instant.fromEpochSeconds(ts)
                                .toLocalDateTime(TimeZone.UTC).year
                            HeroMetaPill(text = year.toString())
                        }
                        game.genres.firstOrNull()?.let { genre ->
                            HeroMetaPill(text = genre)
                        }
                        if (game.platforms.isNotEmpty()) {
                            val platformSummary = game.platforms.take(3).map { formatPlatformName(it) }.joinToString(" · ")
                            HeroMetaPill(text = platformSummary)
                        }
                    }

                    if (!game.summary.isNullOrBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = game.summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = GamingTextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            // Balanced Action Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GamingCard)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (!isSaved) {
                    Button(
                        onClick = onAddToBacklog,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(vertical = 13.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "+ Add to Backlog",
                                color = GamingTextPrimary,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF064E3B).copy(alpha = 0.6f))
                            .border(1.dp, GamingEmerald.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            .clickable(onClick = onAddToBacklog)
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Text("✓", color = GamingEmerald, fontWeight = FontWeight.Bold)
                            Text(
                                text = "In ${libraryStatus?.name ?: "Bucket List"} (Tap to manage)",
                                color = GamingEmerald,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                // Micro-hint
                Text(
                    text = "Tap card for full synopsis, ratings & similar games ↗",
                    style = MaterialTheme.typography.labelSmall,
                    color = GamingTextSecondary.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}
}

// ── 2. Genre Filter Chips ─────────────────────────────────────────────────────

@Composable
private fun GenreFilterRow(
    selectedGenre: String,
    onSelectGenre: (String) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(GENRE_FILTERS) { genre ->
            val isSelected = selectedGenre == genre
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) ElectricGradient else Brush.linearGradient(listOf(GamingCard, GamingCard)))
                    .then(
                        if (isSelected) Modifier
                        else Modifier.border(1.dp, GlassBorderBrush, RoundedCornerShape(12.dp))
                    )
                    .clickable { onSelectGenre(genre) }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
            ) {
                Text(
                    text = genre,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) GamingTextPrimary else GamingTextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                )
            }
        }
    }
}

// ── 3. Genre Empty State ──────────────────────────────────────────────────────

@Composable
private fun GenreEmptyState(
    genre: String,
    onSearchOnline: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = BorderStroke(1.dp, GlassBorderBrush),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(GamingStat),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ObsidianIcons.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = GamingBlue,
                )
            }

            Text(
                text = "No \"$genre\" Games in Current Feed",
                style = MaterialTheme.typography.titleMedium,
                color = GamingTextPrimary,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Discover the global catalog for top $genre titles to add to your bucket list!",
                style = MaterialTheme.typography.bodySmall,
                color = GamingTextSecondary,
                textAlign = TextAlign.Center,
            )

            Button(
                onClick = onSearchOnline,
                colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text("Explore All $genre Games ↗", color = GamingTextPrimary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Modern Section Header (PS5 / Steam Style with Accent Capsule Bar) ────────

@Composable
private fun ModernSectionHeader(
    title: String,
    badge: String,
    accentGradient: Brush,
    count: Int,
) {
    val (badgeBg, badgeColor) = when (badge) {
        "HOT"       -> GamingRed.copy(alpha = 0.18f) to GamingRed
        "TOP RATED" -> GamingGold.copy(alpha = 0.18f) to GamingGold
        "NEW"       -> GamingCyan.copy(alpha = 0.18f) to GamingCyan
        else        -> GamingBlue.copy(alpha = 0.18f) to GamingBlue
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GamingBackground)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Sleek glowing vertical accent capsule
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(accentGradient)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = GamingTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(badgeBg)
                    .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 2.5.dp),
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeColor,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                )
            }
        }

        Text(
            text = "$count games",
            style = MaterialTheme.typography.labelSmall,
            color = GamingTextPrimary.copy(alpha = 0.65f),
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
        )
    }
}

// ── Horizontal Game List ──────────────────────────────────────────────────────

@Composable
private fun HorizontalGameList(
    games: List<Game>,
    libraryGames: List<LibraryEntry> = emptyList(),
    onGameClick: (Game) -> Unit = {},
    onQuickAdd: (Game) -> Unit = {},
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            val libraryEntry = libraryGames.firstOrNull { it.gameId == game.id }
            HorizontalGameCard(
                game = game,
                libraryStatus = libraryEntry?.status,
                onClick = { onGameClick(game) },
                onQuickAdd = { onQuickAdd(game) },
            )
        }
    }
}

// ── Portrait Game Card ────────────────────────────────────────────────────────

@Composable
private fun HorizontalGameCard(
    game: Game,
    libraryStatus: LibraryStatus? = null,
    onClick: () -> Unit = {},
    onQuickAdd: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(190.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, GlassBorderBrush, RoundedCornerShape(16.dp))
                .background(GamingCard),
        ) {
            AsyncImage(
                model = game.coverUrl,
                contentDescription = game.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            // Universal Intuitive Star Rating Badge on Top Right (e.g. "★ 4.9")
            game.rating?.let { rating ->
                val starScore = toStarScore(rating)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(GamingBackground.copy(alpha = 0.88f))
                        .border(1.dp, GamingGold.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = "★ $starScore",
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                    )
                }
            }

            // Platform Pill on Bottom Left (Padded to end = 38.dp so it NEVER overlaps + button)
            if (game.platforms.isNotEmpty()) {
                val formattedPlatform = formatPlatformName(game.platforms.first())
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 6.dp, bottom = 6.dp, end = 38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(GamingBackground.copy(alpha = 0.88f))
                        .border(1.dp, GlassBorderBrush, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = formattedPlatform,
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingTextSecondary,
                        fontSize = 9.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Quick Add / Status button on Bottom Right
            val isSaved = libraryStatus != null
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .size(30.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        if (isSaved) Color(0xFF059669) // Vibrant emerald confirmation
                        else GamingBlue
                    )
                    .border(
                        1.dp,
                        if (isSaved) Color(0xFF34D399) else Color(0x40FFFFFF),
                        RoundedCornerShape(9.dp)
                    )
                    .clickable { onQuickAdd() },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = when (libraryStatus) {
                        LibraryStatus.BACKLOG   -> "✓"
                        LibraryStatus.PLAYING   -> "▶"
                        LibraryStatus.COMPLETED -> "★"
                        null                    -> "+"
                    },
                    color = GamingTextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                )
            }
        }

        Spacer(Modifier.height(7.dp))

        Text(
            text = game.name,
            style = MaterialTheme.typography.bodyMedium,
            color = GamingTextPrimary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        if (game.genres.isNotEmpty()) {
            Spacer(Modifier.height(2.dp))
            Text(
                text = game.genres.first(),
                style = MaterialTheme.typography.labelSmall,
                color = GamingTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ── Hero Meta Pill ────────────────────────────────────────────────────────────

@Composable
private fun HeroMetaPill(text: String, highlight: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (highlight) GamingBlue.copy(alpha = 0.25f)
                else GamingStat.copy(alpha = 0.9f)
            )
            .border(1.dp, GlassBorderBrush, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (highlight) GamingBlue else GamingTextSecondary,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

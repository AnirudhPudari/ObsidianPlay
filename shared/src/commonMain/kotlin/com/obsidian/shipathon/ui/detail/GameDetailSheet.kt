package com.obsidian.shipathon.ui.detail

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.domain.model.SimilarGame
import com.obsidian.shipathon.ui.theme.CardBackdropOverlay
import com.obsidian.shipathon.ui.theme.ElectricGradient
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingCardElevated
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailSheet(
    game: Game,
    libraryEntry: LibraryEntry?,
    onDismiss: () -> Unit,
    onStatusChange: (LibraryStatus) -> Unit,
    onProgressUpdate: (progressPercent: Int, playtimeHours: Double) -> Unit,
    onRemoveFromLibrary: () -> Unit,
    onOpenShareCard: (Game) -> Unit = {},
    onSimilarGameClick: (Game) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var currentProgress by remember(libraryEntry) {
        mutableStateOf(libraryEntry?.progressPercent?.toFloat() ?: 0f)
    }
    var currentPlaytime by remember(libraryEntry) {
        mutableStateOf(libraryEntry?.playtimeHours ?: 0.0)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GamingBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GamingStat),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── 1. Hero Artwork Header ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, GlassBorderBrush, RoundedCornerShape(24.dp))
                    .background(GamingCard),
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

                // Star Rating Badge
                game.rating?.let { rating ->
                    val starScore = ((if (rating <= 5.0) rating else rating / 20.0) * 10).toInt() / 10.0
                    val (badgeText, badgeColor) = when {
                        rating >= 88.0 -> "★ $starScore / 5.0 • Universal Acclaim" to GamingGold
                        rating >= 75.0 -> "★ $starScore / 5.0 • Highly Rated" to GamingBlue
                        else           -> "★ $starScore / 5.0" to GamingGold
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingBackground.copy(alpha = 0.88f))
                            .border(1.dp, badgeColor.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeColor,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }

            // ── 2. Title & Meta Tags ──────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = GamingTextPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.6).sp,
                )

                // Meta Pill Badges (Year · Genres) - Clean FlowRow Layout
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    game.firstReleaseDate?.let { ts ->
                        val year = Instant.fromEpochSeconds(ts)
                            .toLocalDateTime(TimeZone.UTC).year
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GamingStat)
                                .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(8.dp))
                                .padding(horizontal = 9.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = "Released $year",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingTextSecondary,
                                fontSize = 11.sp,
                            )
                        }
                    }

                    game.genres.forEach { genre ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GamingStat)
                                .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(8.dp))
                                .padding(horizontal = 9.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = genre,
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingTextSecondary,
                                fontSize = 11.sp,
                            )
                        }
                    }
                }
            }

            // ── 3. Platforms Showcase ─────────────────────────────────────────
            if (game.platforms.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GamingCard),
                    border = BorderStroke(1.dp, GlassBorderBrush),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GamingStat),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Gamepad,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = GamingBlue,
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "AVAILABLE PLATFORMS",
                                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                                color = GamingTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                            )
                            Text(
                                text = game.platforms.joinToString(" · "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = GamingTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            // ── 3.5 Estimated Campaign Length ────────────────────────────────
            val isRpg = game.genres.any { it.contains("RPG", ignoreCase = true) || it.contains("Role-playing", ignoreCase = true) }
            val isIndie = game.genres.any { it.contains("Platform", ignoreCase = true) || it.contains("Indie", ignoreCase = true) }
            val isAdventure = game.genres.any { it.contains("Adventure", ignoreCase = true) || it.contains("Action", ignoreCase = true) }

            val mainStoryEst = when {
                isRpg -> "35 – 60 hrs"
                isIndie -> "6 – 12 hrs"
                isAdventure -> "15 – 25 hrs"
                else -> "12 – 20 hrs"
            }
            val completionistEst = when {
                isRpg -> "90 – 140 hrs"
                isIndie -> "18 – 30 hrs"
                else -> "35 – 60 hrs"
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCardElevated),
                border = BorderStroke(1.dp, GlassBorderBrush),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🗡️ Main Story", style = MaterialTheme.typography.labelSmall, color = GamingTextSecondary, fontSize = 10.sp)
                        Text(mainStoryEst, style = MaterialTheme.typography.titleMedium, color = GamingCyan, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(GamingStat))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏆 Completionist (100%)", style = MaterialTheme.typography.labelSmall, color = GamingTextSecondary, fontSize = 10.sp)
                        Text(completionistEst, style = MaterialTheme.typography.titleMedium, color = GamingGold, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ── 4. Bucket List Status Selector ────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "BUCKET LIST STATUS",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                    color = GamingTextSecondary,
                    fontWeight = FontWeight.Bold,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LibraryStatus.entries.forEach { status ->
                        val isSelected = libraryEntry?.status == status
                        val animatedScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.04f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium,
                            ),
                            label = "StatusScale_${status.name}",
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .scale(animatedScale)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) ElectricGradient else Brush.linearGradient(listOf(GamingCard, GamingCard)))
                                .then(
                                    if (isSelected) Modifier
                                    else Modifier.border(1.dp, GlassBorderBrush, RoundedCornerShape(14.dp))
                                )
                                .clickable { onStatusChange(status) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                if (isSelected) {
                                    Text(
                                        text = "✓",
                                        color = GamingTextPrimary,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                Text(
                                    text = when (status) {
                                        LibraryStatus.BACKLOG   -> "Backlog"
                                        LibraryStatus.PLAYING   -> "Playing"
                                        LibraryStatus.COMPLETED -> "Completed"
                                    },
                                    color = if (isSelected) GamingTextPrimary else GamingTextSecondary,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                )
                            }
                        }
                    }
                }
            }


            // ── 5. Campaign Progress & Playtime Editor ────────────────────────
            if (libraryEntry?.status == LibraryStatus.PLAYING || libraryEntry?.status == LibraryStatus.COMPLETED) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = GamingCardElevated),
                    border = BorderStroke(1.dp, GlassBorderBrush),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Campaign Progress",
                                style = MaterialTheme.typography.titleSmall,
                                color = GamingTextPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "${currentProgress.toInt()}%",
                                style = MaterialTheme.typography.titleSmall,
                                color = GamingBlue,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Slider(
                            value = currentProgress,
                            onValueChange = { currentProgress = it },
                            onValueChangeFinished = {
                                onProgressUpdate(currentProgress.toInt(), currentPlaytime)
                            },
                            valueRange = 0f..100f,
                            thumb = {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(GamingCyan)
                                        .border(2.dp, Color.White, CircleShape),
                                )
                            },
                            track = { sliderState ->
                                SliderDefaults.Track(
                                    sliderState = sliderState,
                                    drawStopIndicator = null,
                                    thumbTrackGapSize = 0.dp,
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = GamingCyan,
                                        inactiveTrackColor = GamingStat,
                                    ),
                                )
                            },
                        )

                        // Playtime Stepper
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Hours Logged",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GamingTextSecondary,
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GamingStat)
                                        .border(1.dp, GlassBorderBrush, RoundedCornerShape(8.dp))
                                        .clickable {
                                            if (currentPlaytime >= 1.0) {
                                                currentPlaytime -= 1.0
                                                onProgressUpdate(currentProgress.toInt(), currentPlaytime)
                                            }
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                ) {
                                    Text("-1h", color = GamingTextPrimary, fontWeight = FontWeight.Bold)
                                }

                                Text(
                                    text = "${currentPlaytime.toInt()}h",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GamingTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GamingStat)
                                        .border(1.dp, GlassBorderBrush, RoundedCornerShape(8.dp))
                                        .clickable {
                                            currentPlaytime += 1.0
                                            onProgressUpdate(currentProgress.toInt(), currentPlaytime)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                ) {
                                    Text("+1h", color = GamingBlue, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // ── 6. Full Synopsis ──────────────────────────────────────────────
            if (!game.summary.isNullOrBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "SYNOPSIS",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                        color = GamingTextSecondary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = game.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GamingTextPrimary.copy(alpha = 0.9f),
                        lineHeight = 22.sp,
                    )
                }
            }

            // ── 7. "You Might Also Like" Similar Games Shelf ──────────────────
            if (game.similarGames.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "YOU MIGHT ALSO LIKE",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                        color = GamingTextSecondary,
                        fontWeight = FontWeight.Bold,
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 4.dp),
                    ) {
                        items(game.similarGames) { similar ->
                            SimilarGameMiniCard(
                                similar = similar,
                                onClick = {
                                    onSimilarGameClick(
                                        Game(
                                            id = similar.id,
                                            name = similar.name,
                                            summary = null,
                                            coverUrl = similar.coverUrl,
                                            genres = emptyList(),
                                            platforms = emptyList(),
                                            themes = emptyList(),
                                            rating = null,
                                            firstReleaseDate = null,
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }

            // ── 8. Full-Width Share Gamer Story Card & Quick Actions ─────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = { onOpenShareCard(game) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = ObsidianIcons.Sparkles,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = GamingTextPrimary,
                        )
                        Text(
                            text = "Share Gamer Story Card",
                            color = GamingTextPrimary,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                if (libraryEntry != null) {
                    TextButton(
                        onClick = onRemoveFromLibrary,
                        modifier = Modifier.padding(top = 2.dp),
                    ) {
                        Text(
                            text = "Remove from My Library",
                            color = GamingRed.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SimilarGameMiniCard(
    similar: SimilarGame,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(105.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .width(105.dp)
                .height(145.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, GlassBorderBrush, RoundedCornerShape(12.dp))
                .background(GamingCard),
        ) {
            AsyncImage(
                model = similar.coverUrl,
                contentDescription = similar.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        Text(
            text = similar.name,
            style = MaterialTheme.typography.bodySmall,
            color = GamingTextPrimary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 11.sp,
        )
    }
}

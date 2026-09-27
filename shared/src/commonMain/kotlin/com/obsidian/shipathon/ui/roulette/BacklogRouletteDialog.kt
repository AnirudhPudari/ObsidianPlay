package com.obsidian.shipathon.ui.roulette

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.ui.theme.CyanGradient
import com.obsidian.shipathon.ui.theme.DialogBorderGlowBrush
import com.obsidian.shipathon.ui.theme.DialogSurfaceGradient
import com.obsidian.shipathon.ui.theme.ElectricGradient
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingCardElevated
import com.obsidian.shipathon.ui.theme.GamingCyan
import com.obsidian.shipathon.ui.theme.GamingEmerald
import com.obsidian.shipathon.ui.theme.GamingGold
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.ObsidianIcons
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BacklogRouletteDialog(
    backlogGames: List<LibraryEntry>,
    onDismiss: () -> Unit,
    onStartPlaying: (LibraryEntry) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }
    var selectedGame by remember { mutableStateOf(backlogGames.randomOrNull()) }

    val spinScale by animateFloatAsState(
        targetValue = if (isSpinning) 0.96f else 1f,
        animationSpec = tween(150),
        label = "SpinScaleAnim",
    )

    fun spinRoulette() {
        if (backlogGames.isEmpty() || isSpinning) return
        isSpinning = true
        coroutineScope.launch {
            val cycles = 18
            for (i in 0 until cycles) {
                selectedGame = backlogGames.random()
                val delayTime = (30 + (i * i * 2)).toLong()
                delay(delayTime)
            }
            isSpinning = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Header Bar with Dice Emblem
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(end = 36.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CyanGradient),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Dice,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = GamingTextPrimary,
                            )
                        }

                        Column {
                            Text(
                                text = "Backlog Roulette",
                                style = MaterialTheme.typography.titleMedium,
                                color = GamingTextPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Instant Decision Maker",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingCyan,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                            )
                        }
                    }

                    // ── Animated Center Game Showcase Card ────────────────────
                    val game = selectedGame
                    if (game != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scale(spinScale),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = GamingCardElevated),
                            border = if (isSpinning) {
                                BorderStroke(1.2.dp, ElectricGradient)
                            } else {
                                BorderStroke(1.dp, Color(0x20FFFFFF))
                            },
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                // High-Res Artwork Cover
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(170.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(GamingCard),
                                ) {
                                    AsyncImage(
                                        model = game.coverUrl,
                                        contentDescription = game.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop,
                                    )

                                    // Rating Pill
                                    game.rating?.let { rating ->
                                        val score = ((if (rating <= 5.0) rating else rating / 20.0) * 10).toInt() / 10.0
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(8.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(GamingBackground.copy(alpha = 0.85f))
                                                .border(1.dp, GamingGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 7.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        ) {
                                            Icon(
                                                imageVector = ObsidianIcons.Star,
                                                contentDescription = null,
                                                modifier = Modifier.size(12.dp),
                                                tint = GamingGold,
                                            )
                                            Text(
                                                text = "$score",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = GamingGold,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }
                                }

                                // Title & Genre Metadata
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(3.dp),
                                ) {
                                    Text(
                                        text = game.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = GamingTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    if (game.genres.isNotEmpty()) {
                                        Text(
                                            text = game.genres.take(2).joinToString(" • "),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GamingTextSecondary,
                                            fontSize = 11.sp,
                                        )
                                    }

                                    if (game.platforms.isNotEmpty()) {
                                        Text(
                                            text = game.platforms.take(2).joinToString(" · "),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GamingCyan,
                                            fontSize = 10.sp,
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Empty Backlog state
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(GamingCardElevated),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Your backlog is empty! Add games first.",
                                color = GamingTextSecondary,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }

                    // Action Buttons (Start Playing & Spin)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Button(
                            onClick = {
                                selectedGame?.let {
                                    onStartPlaying(it)
                                    onDismiss()
                                }
                            },
                            enabled = !isSpinning && selectedGame != null,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    imageVector = ObsidianIcons.Gamepad,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = GamingTextPrimary,
                                )
                                Text(
                                    text = "Start Playing This Game",
                                    color = GamingTextPrimary,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }

                        Button(
                            onClick = { spinRoulette() },
                            enabled = !isSpinning && backlogGames.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = GamingStat),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0x18FFFFFF)),
                            contentPadding = PaddingValues(vertical = 11.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    imageVector = ObsidianIcons.Dice,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = GamingTextPrimary,
                                )
                                Text(
                                    text = if (isSpinning) "Selecting randomly..." else "Spin Again (Reroll)",
                                    color = GamingTextPrimary,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
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

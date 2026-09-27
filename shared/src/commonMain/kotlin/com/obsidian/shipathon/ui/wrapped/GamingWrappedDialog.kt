package com.obsidian.shipathon.ui.wrapped

import com.obsidian.shipathon.rememberShareLauncher

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryStatus
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
import com.obsidian.shipathon.ui.theme.GoldGradient
import com.obsidian.shipathon.ui.theme.ObsidianIcons

enum class WrappedTheme(
    val label: String,
    val brush: Brush,
    val accentColor: Color,
    val isProOnly: Boolean,
) {
    Electric(
        label = "Electric Neon",
        brush = Brush.verticalGradient(listOf(Color(0xFF0F1A30), Color(0xFF070D1C))),
        accentColor = GamingCyan,
        isProOnly = false,
    ),
    CyberGold(
        label = "Cyber Gold 👑",
        brush = Brush.verticalGradient(listOf(Color(0xFF281E08), Color(0xFF0F0B03))),
        accentColor = GamingGold,
        isProOnly = true,
    ),
    EmeraldMatrix(
        label = "Matrix Green 👑",
        brush = Brush.verticalGradient(listOf(Color(0xFF072418), Color(0xFF030F0A))),
        accentColor = GamingEmerald,
        isProOnly = true,
    ),
}

@Composable
fun GamingWrappedDialog(
    libraryGames: List<LibraryEntry>,
    isPro: Boolean,
    onDismiss: () -> Unit,
    onOpenPaywall: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf(WrappedTheme.Electric) }

    val completedGames = libraryGames.filter { it.status == LibraryStatus.COMPLETED }
    val totalHours = libraryGames.sumOf { it.playtimeHours }
    val totalCount = libraryGames.size

    // Calculate Persona
    val genreCounts = mutableMapOf<String, Int>()
    libraryGames.forEach { entry ->
        entry.genres.forEach { genre ->
            genreCounts[genre] = (genreCounts[genre] ?: 0) + 1
        }
    }
    val topGenre = genreCounts.maxByOrNull { it.value }?.key ?: "Gamer"
    val gamerPersona = when {
        topGenre.contains("RPG", ignoreCase = true) || topGenre.contains("Role-playing", ignoreCase = true) -> "Legendary RPG Sorcerer"
        topGenre.contains("Action", ignoreCase = true) -> "Adrenaline Action Striker"
        topGenre.contains("Adventure", ignoreCase = true) -> "Mythic Realm Explorer"
        topGenre.contains("Strategy", ignoreCase = true) -> "Master Tactician"
        topGenre.contains("Indie", ignoreCase = true) || topGenre.contains("Platform", ignoreCase = true) -> "Indie Gem Pioneer"
        else -> "Elite Backlog Master"
    }

    val gamerLevel = (completedGames.size * 2) + (totalCount / 2).coerceAtLeast(1)

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 36.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldGradient),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("👑", fontSize = 20.sp)
                        }

                        Column {
                            Text(
                                text = "Gamer Wrapped 2026",
                                style = MaterialTheme.typography.titleMedium,
                                color = GamingTextPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Formatted for Instagram Stories & TikTok",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingGold,
                                fontSize = 10.sp,
                            )
                        }
                    }

                    // Theme selector chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        WrappedTheme.entries.forEach { theme ->
                            val isSelected = selectedTheme == theme
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) theme.accentColor.copy(alpha = 0.25f) else GamingStat)
                                    .then(
                                        if (isSelected) Modifier.border(1.dp, theme.accentColor, RoundedCornerShape(10.dp))
                                        else Modifier.border(1.dp, GlassBorderBrush, RoundedCornerShape(10.dp))
                                    )
                                    .clickable {
                                        if (theme.isProOnly && !isPro) {
                                            onOpenPaywall()
                                        } else {
                                            selectedTheme = theme
                                        }
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = theme.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) theme.accentColor else GamingTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 9.sp,
                                )
                            }
                        }
                    }

                    // ── 9:16 Social Wrapped Showcase Card ─────────────────────
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = GamingCardElevated),
                        border = BorderStroke(1.5.dp, selectedTheme.accentColor.copy(alpha = 0.7f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(selectedTheme.brush)
                                .padding(18.dp),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                            ) {
                                // Top Header Brand
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "⚡ OBSIDIANPLAY WRAPPED",
                                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                                        color = selectedTheme.accentColor,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                    )
                                    Text(
                                        text = "2026 EDITION",
                                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                                        color = GamingTextSecondary,
                                        fontSize = 8.5.sp,
                                    )
                                }

                                // Player Persona & Level Badge
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(if (isPro) GoldGradient else ElectricGradient),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(if (isPro) "👑" else "🎮", fontSize = 24.sp)
                                    }
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = gamerPersona,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = GamingTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                    )
                                    Text(
                                        text = "Level $gamerLevel • ${if (isPro) "PRO VIP Founder" else "Gamer"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = selectedTheme.accentColor,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }

                                // 3 Key Metric Stats Grid
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    WrappedStatPill(
                                        modifier = Modifier.weight(1f),
                                        number = "${completedGames.size}",
                                        label = "Conquered",
                                        accentColor = selectedTheme.accentColor,
                                    )
                                    WrappedStatPill(
                                        modifier = Modifier.weight(1f),
                                        number = "${totalHours.toInt()}h",
                                        label = "Playtime",
                                        accentColor = selectedTheme.accentColor,
                                    )
                                    WrappedStatPill(
                                        modifier = Modifier.weight(1f),
                                        number = "$totalCount",
                                        label = "In Backlog",
                                        accentColor = selectedTheme.accentColor,
                                    )
                                }

                                // Conquered Hall of Fame posters (up to 3)
                                if (completedGames.isNotEmpty()) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        Text(
                                            text = "CONQUERED MASTERPIECES",
                                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                                            color = GamingTextSecondary,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        ) {
                                            completedGames.take(3).forEach { gameEntry ->
                                                Card(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(95.dp),
                                                    shape = RoundedCornerShape(10.dp),
                                                    colors = CardDefaults.cardColors(containerColor = GamingCard),
                                                    border = BorderStroke(1.dp, GlassBorderBrush),
                                                ) {
                                                    AsyncImage(
                                                        model = gameEntry.coverUrl,
                                                        contentDescription = gameEntry.name,
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentScale = ContentScale.Crop,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // Watermark
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GamingBackground.copy(alpha = 0.8f))
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "TRACKED WITH OBSIDIANPLAY APP",
                                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                                        color = GamingTextSecondary,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                    }

                    // Share Actions (Native Share Sheet + Copy Caption)
                    val shareLauncher = rememberShareLauncher()
                    val shareCaption = "My 2026 Gaming Wrapped on ObsidianPlay! 🎮 Conquered ${completedGames.size} games with ${totalHours.toInt()}h logged. My persona: $gamerPersona! #GamingWrapped #ObsidianPlay #Gaming2026 #Gamer"

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(shareCaption))
                                shareLauncher(shareCaption, "Share Gaming Wrapped 2026")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 13.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    imageVector = ObsidianIcons.Sparkles,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = GamingTextPrimary,
                                )
                                Text(
                                    text = "Share to Instagram, TikTok & Apps",
                                    color = GamingTextPrimary,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }

                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(shareCaption))
                                copied = true
                            },
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
                                if (copied) {
                                    Icon(
                                        imageVector = ObsidianIcons.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = GamingEmerald,
                                    )
                                }
                                Text(
                                    text = if (copied) "Story Caption Copied!" else "Copy Story Caption & Hashtags",
                                    color = if (copied) GamingEmerald else GamingTextSecondary,
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

@Composable
private fun WrappedStatPill(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    accentColor: Color,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GamingBackground.copy(alpha = 0.85f))
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.titleMedium,
                color = accentColor,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = GamingTextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

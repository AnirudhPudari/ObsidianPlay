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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.rememberShareLauncher
import com.obsidian.shipathon.ui.theme.CardBackdropOverlay
import com.obsidian.shipathon.ui.theme.CyberObsidianPreset
import com.obsidian.shipathon.ui.theme.DialogBorderGlowBrush
import com.obsidian.shipathon.ui.theme.DialogSurfaceGradient
import com.obsidian.shipathon.ui.theme.ElectricGradient
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingCyan
import com.obsidian.shipathon.ui.theme.GamingEmerald
import com.obsidian.shipathon.ui.theme.GamingGold
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.NeonEmeraldPreset
import com.obsidian.shipathon.ui.theme.ObsidianIcons
import com.obsidian.shipathon.ui.theme.SunsetFlamePreset

private enum class CardTheme(val label: String, val brush: Brush, val accentColor: Color, val isProOnly: Boolean = false) {
    Cyber("Cyber Obsidian", CyberObsidianPreset, GamingBlue, isProOnly = false),
    Emerald("Neon Emerald 👑", NeonEmeraldPreset, GamingEmerald, isProOnly = true),
    Sunset("Sunset Flame 👑", SunsetFlamePreset, Color(0xFFFF3366), isProOnly = true),
}

@Composable
fun ShareCardDialog(
    game: Game,
    isPro: Boolean = false,
    onDismiss: () -> Unit,
    onOpenPaywall: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf(CardTheme.Cyber) }

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(
                            imageVector = ObsidianIcons.Sparkles,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = GamingCyan,
                        )
                        Column {
                            Text(
                                text = "Gamer Story Card",
                                style = MaterialTheme.typography.titleMedium,
                                color = GamingTextPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Formatted for Instagram Stories & TikTok",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingCyan,
                                fontSize = 10.sp,
                            )
                        }
                    }

                    // Theme Preset Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        CardTheme.entries.forEach { theme ->
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

                // ── 9:16 Visual Story Showcase Card ───────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = GamingCard),
                    border = BorderStroke(1.5.dp, selectedTheme.accentColor.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(selectedTheme.brush),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            // Top Watermark Pill
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GamingBackground.copy(alpha = 0.9f))
                                        .border(1.dp, GlassBorderBrush, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Icon(
                                        imageVector = ObsidianIcons.Gamepad,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = selectedTheme.accentColor,
                                    )
                                    Text(
                                        text = "OBSIDIANPLAY",
                                        color = GamingTextPrimary,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.5.sp,
                                        fontSize = 10.sp,
                                    )
                                }

                                game.rating?.let { rating ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(GamingBackground.copy(alpha = 0.9f))
                                            .border(1.dp, selectedTheme.accentColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                    ) {
                                        Text(
                                            text = "Score: ${rating.toInt()}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = selectedTheme.accentColor,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }

                            // Center High-Res Artwork
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(230.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .border(1.dp, GlassBorderBrush, RoundedCornerShape(18.dp)),
                            ) {
                                AsyncImage(
                                    model = game.coverUrl,
                                    contentDescription = game.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            }

                            // Game Title & Metadata
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = game.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = GamingTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    letterSpacing = (-0.4).sp,
                                )

                                if (game.genres.isNotEmpty()) {
                                    Text(
                                        text = game.genres.take(3).joinToString(" • "),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GamingTextSecondary,
                                        textAlign = TextAlign.Center,
                                    )
                                }

                                if (game.platforms.isNotEmpty()) {
                                    Text(
                                        text = game.platforms.take(3).joinToString(" · "),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = selectedTheme.accentColor,
                                        fontSize = 10.sp,
                                    )
                                }
                            }

                            // Bottom Gamer Bucket List Watermark
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(GamingBackground.copy(alpha = 0.8f))
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "TRACKED IN MY GAMING BUCKET LIST",
                                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                                    color = GamingTextSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }

                // Share Actions (Native Share Sheet + Copy Caption)
                val shareLauncher = rememberShareLauncher()
                val shareText = "I'm tracking \"${game.name}\" on my gaming bucket list with ObsidianPlay! 🎮🔥 #gaming #gametracker #backlog #${game.name.replace(" ", "").replace(":", "").replace("-", "")}"

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(shareText))
                            shareLauncher(shareText, "Share \"${game.name}\" Story Card")
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
                            clipboardManager.setText(AnnotatedString(shareText))
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

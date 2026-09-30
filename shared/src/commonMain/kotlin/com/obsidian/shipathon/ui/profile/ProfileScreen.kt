package com.obsidian.shipathon.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.ui.theme.CyanGradient
import com.obsidian.shipathon.ui.theme.ElectricGradient
import com.obsidian.shipathon.ui.theme.GamingBackground
import com.obsidian.shipathon.ui.theme.GamingBackgroundNebula
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCard
import com.obsidian.shipathon.ui.theme.GamingCardElevated
import com.obsidian.shipathon.ui.theme.GamingCyan
import com.obsidian.shipathon.ui.theme.GamingEmerald
import com.obsidian.shipathon.ui.theme.GamingGold
import com.obsidian.shipathon.ui.theme.GamingRed
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextMuted
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.GoldGradient
import com.obsidian.shipathon.ui.theme.ObsidianIcons

@Composable
fun ProfileScreen(
    libraryGames: List<LibraryEntry> = emptyList(),
    isPro: Boolean = false,
    onOpenPaywall: () -> Unit = {},
    onOpenWrapped: () -> Unit = {},
    onToggleJudgePro: ((Boolean) -> Unit)? = null,
) {
    var showPromoDialog by remember { mutableStateOf(false) }
    val backlogCount = libraryGames.count { it.status == LibraryStatus.BACKLOG }
    val playingCount = libraryGames.count { it.status == LibraryStatus.PLAYING }
    val completedCount = libraryGames.count { it.status == LibraryStatus.COMPLETED }
    val totalHours = libraryGames.sumOf { it.playtimeHours }

    val gamerLevel = (completedCount * 3 + playingCount * 2 + (totalHours / 10).toInt()).coerceAtLeast(1)
    val gamerTitle = when {
        completedCount >= 15 -> "Master Conqueror"
        completedCount >= 8 -> "Backlog Slayer"
        completedCount >= 3 -> "Dedicated Gamer"
        else -> "Novice Explorer"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingBackgroundNebula)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // ── 1. Unified Hero Gamer Identity & Stats Hub ────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = GamingCard),
            border = if (isPro) BorderStroke(1.2.dp, GamingGold.copy(alpha = 0.7f)) else BorderStroke(1.dp, GlassBorderBrush),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Identity Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    // Avatar circle with Gradient
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(if (isPro) GoldGradient else ElectricGradient),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = if (isPro) ObsidianIcons.Trophy else ObsidianIcons.Gamepad,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = if (isPro) GamingBackground else GamingTextPrimary,
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "Obsidian Gamer",
                                style = MaterialTheme.typography.titleLarge,
                                color = GamingTextPrimary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.3).sp,
                            )
                            if (isPro) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldGradient)
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                ) {
                                    Text(
                                        text = "PRO VIP",
                                        color = GamingBackground,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp,
                                    )
                                }
                            }
                        }
                        Text(
                            text = "$gamerTitle • Level $gamerLevel",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isPro) GamingGold else GamingBlue,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                // Integrated 4-Stat Glance Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    MiniStatItem(
                        label = "Backlog",
                        value = backlogCount.toString(),
                        icon = ObsidianIcons.Home,
                        iconTint = GamingCyan,
                        modifier = Modifier.weight(1f),
                    )
                    MiniStatItem(
                        label = "Playing",
                        value = playingCount.toString(),
                        icon = ObsidianIcons.Gamepad,
                        iconTint = GamingBlue,
                        modifier = Modifier.weight(1f),
                    )
                    MiniStatItem(
                        label = "Conquered",
                        value = completedCount.toString(),
                        icon = ObsidianIcons.Trophy,
                        iconTint = GamingGold,
                        modifier = Modifier.weight(1f),
                    )
                    MiniStatItem(
                        label = "Logged",
                        value = "${totalHours.toInt()}h",
                        icon = ObsidianIcons.Search,
                        iconTint = GamingTextSecondary,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        // ── 2. Single PRO Upgrade Card (Only if Free) ────────────────────────
        if (!isPro) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenPaywall),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCardElevated),
                border = BorderStroke(1.2.dp, GamingGold.copy(alpha = 0.8f)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldGradient),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Crown,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                                tint = GamingBackground,
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Upgrade to Obsidian PRO",
                                style = MaterialTheme.typography.titleSmall,
                                color = GamingGold,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Unlock 2X XP, story themes & smart roulette",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingTextSecondary,
                                fontSize = 11.sp,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GoldGradient)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "PRO ↗",
                            color = GamingBackground,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }

        // ── 3. Compact Gaming DNA Top Genres ─────────────────────────────────
        CompactGamingDna(entries = libraryGames)

        // ── 4. Clean App Information & Settings ──────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GamingCard),
            border = BorderStroke(1.dp, GlassBorderBrush),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (isPro) {
                    SettingRow(
                        icon = ObsidianIcons.Crown,
                        iconTint = GamingGold,
                        iconBg = GamingGold.copy(alpha = 0.15f),
                        title = "Membership Status",
                        subtitle = "Obsidian PRO VIP Founder",
                        actionText = "ACTIVE",
                        actionColor = GamingGold,
                        onClick = null,
                    )
                    HorizontalDivider(color = GamingStat, thickness = 1.dp)
                }

                if (!isPro) {
                    SettingRow(
                        icon = ObsidianIcons.Ticket,
                        iconTint = GamingCyan,
                        iconBg = GamingCyan.copy(alpha = 0.15f),
                        title = "Redeem Promo Code",
                        subtitle = "VIP invite or creator promo code",
                        actionText = "REDEEM",
                        actionColor = GamingCyan,
                        onClick = { showPromoDialog = true },
                    )
                    HorizontalDivider(color = GamingStat, thickness = 1.dp)
                }

                // App Version & Legal Attribution
                SettingRow(
                    icon = ObsidianIcons.Info,
                    iconTint = GamingBlue,
                    iconBg = GamingBlue.copy(alpha = 0.15f),
                    title = "ObsidianPlay",
                    subtitle = "v1.0.0 Production • Data powered by IGDB",
                    actionText = "",
                    actionColor = GamingEmerald,
                    onClick = null,
                )
            }
        }

        // Promo Code Dialog
        if (showPromoDialog) {
            Dialog(onDismissRequest = { showPromoDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .border(1.2.dp, Color(0x6000E5FF), RoundedCornerShape(22.dp)),
                    color = GamingCardElevated,
                    shadowElevation = 24.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0x2500E5FF))
                                .border(1.dp, Color(0x6000E5FF), CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Ticket,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = GamingCyan,
                            )
                        }

                        Text(
                            text = "Redeem Promo Code",
                            style = MaterialTheme.typography.titleMedium,
                            color = GamingTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                        )

                        Text(
                            text = "Enter your VIP invite code or promo code to unlock Obsidian PRO.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GamingTextSecondary,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                        )

                        var codeInput by remember { mutableStateOf("") }
                        var errorMessage by remember { mutableStateOf<String?>(null) }
                        val validCodes = remember {
                            setOf("DEVPOST2026", "DEVPOST", "REVENUECAT", "OBSIDIANPRO", "SHIPATHON2026", "SHIPATHON", "JUDGE", "VIP2026", "PROMO")
                        }

                        // Text Input
                        OutlinedTextField(
                            value = codeInput,
                            onValueChange = {
                                codeInput = it.uppercase().trim()
                                errorMessage = null
                            },
                            placeholder = {
                                Text("e.g. OBSIDIANPRO", color = GamingTextMuted, fontSize = 13.sp)
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = GamingTextPrimary,
                                unfocusedTextColor = GamingTextPrimary,
                                focusedBorderColor = GamingCyan,
                                unfocusedBorderColor = Color(0x35FFFFFF),
                                focusedContainerColor = GamingBackground,
                                unfocusedContainerColor = GamingBackground,
                            ),
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage ?: "",
                                color = GamingRed,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.5.sp,
                            )
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            OutlinedButton(
                                onClick = { showPromoDialog = false },
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0x25FFFFFF)),
                            ) {
                                Text("Cancel", color = GamingTextSecondary, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    if (validCodes.contains(codeInput)) {
                                        onToggleJudgePro?.invoke(true)
                                        showPromoDialog = false
                                    } else {
                                        errorMessage = "Invalid promo code. Please check and try again."
                                    }
                                },
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                            ) {
                                Text("Redeem", color = GamingTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Mini Stat Pill Component ─────────────────────────────────────────────────

@Composable
private fun MiniStatItem(
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
            .padding(vertical = 10.dp, horizontal = 4.dp),
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
                modifier = Modifier.size(13.dp),
                tint = iconTint,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = GamingTextPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = GamingTextSecondary,
            fontSize = 10.sp,
        )
    }
}

// ── Compact Gaming DNA Flavor ────────────────────────────────────────────────

@Composable
private fun CompactGamingDna(entries: List<LibraryEntry>) {
    val genreCounts = mutableMapOf<String, Int>()
    entries.forEach { entry ->
        entry.genres.forEach { genre ->
            genreCounts[genre] = (genreCounts[genre] ?: 0) + 1
        }
    }

    val totalGenres = genreCounts.values.sum().coerceAtLeast(1)
    val topGenres = genreCounts.entries
        .sortedByDescending { it.value }
        .take(3)

    if (topGenres.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GamingCard),
            border = BorderStroke(1.dp, GlassBorderBrush),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GamingCyan.copy(alpha = 0.15f))
                                .border(1.dp, GamingCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Dna,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = GamingCyan,
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                            Text(
                                text = "Gaming DNA",
                                style = MaterialTheme.typography.titleSmall,
                                color = GamingTextPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Top genres calculated from library",
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingTextSecondary,
                                fontSize = 10.5.sp,
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GamingCyan.copy(alpha = 0.12f))
                            .border(1.dp, GamingCyan.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = "${entries.size} Games",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    topGenres.forEachIndexed { index, (genre, count) ->
                        val percent = ((count.toFloat() / totalGenres.toFloat()) * 100).toInt().coerceAtLeast(5)
                        val accentColor = when (index) {
                            0 -> GamingCyan
                            1 -> GamingBlue
                            else -> GamingEmerald
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(accentColor.copy(alpha = 0.15f))
                                .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .padding(vertical = 8.dp, horizontal = 6.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                Text(
                                    text = genre,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GamingTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = "$percent%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = accentColor,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Clean Grouped Setting Row ────────────────────────────────────────────────

@Composable
private fun SettingRow(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    actionText: String,
    actionColor: Color,
    onClick: (() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg)
                    .border(1.dp, iconTint.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = iconTint,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GamingTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = GamingTextSecondary,
                    fontSize = 11.sp,
                )
            }
        }

        if (actionText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(actionColor.copy(alpha = 0.15f))
                    .border(1.dp, actionColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = actionText,
                    color = actionColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                )
            }
        }
    }
}


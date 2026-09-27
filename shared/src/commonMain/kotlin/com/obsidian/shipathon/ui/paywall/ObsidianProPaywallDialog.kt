package com.obsidian.shipathon.ui.paywall

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.obsidian.shipathon.domain.subscription.SubscriptionPlan
import com.obsidian.shipathon.ui.theme.DialogBorderGlowBrush
import com.obsidian.shipathon.ui.theme.DialogSurfaceGradient
import com.obsidian.shipathon.ui.theme.GamingBackground
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
import com.obsidian.shipathon.ui.theme.GamingViolet
import com.obsidian.shipathon.ui.theme.ObsidianIcons

@Composable
fun ObsidianProPaywallDialog(
    isPro: Boolean,
    dynamicPrices: Map<SubscriptionPlan, String> = emptyMap(),
    onDismiss: () -> Unit,
    onPurchasePlan: (SubscriptionPlan) -> Unit,
    onRestorePurchases: () -> Unit,
    onToggleJudgePro: (Boolean) -> Unit,
) {
    var selectedPlan by remember { mutableStateOf(SubscriptionPlan.ANNUAL) }
    var showPromoDialog by remember { mutableStateOf(false) }

    // Subtle breathing glow animation for the hero emblem
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(28.dp))
                .border(
                    BorderStroke(
                        1.2.dp,
                        Brush.verticalGradient(
                            listOf(
                                Color(0x668B5CF6),
                                Color(0x3306B6D4),
                                Color(0x15FFFFFF),
                            )
                        )
                    ),
                    RoundedCornerShape(28.dp)
                ),
            color = GamingBackground,
            shadowElevation = 32.dp,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF161933),
                                Color(0xFF0F1426),
                                Color(0xFF090D1A),
                            )
                        )
                    )
            ) {
                // Top Ambient Radial Glow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    GamingViolet.copy(alpha = glowAlpha * 0.4f),
                                    GamingBlue.copy(alpha = 0.15f),
                                    Color.Transparent,
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(13.dp),
                ) {
                    // ── 1. Hologram Hero Header ──────────────────────────────
                    PaywallHeroHeader()

                    // ── 2. Feature Showcase Cards ────────────────────────────
                    ProFeaturesShowcase()

                    // ── 3. Plan Selection Cards ──────────────────────────────
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        SubscriptionPlan.entries.forEach { plan ->
                            val isSelected = selectedPlan == plan
                            ProPlanCard(
                                plan = plan,
                                isSelected = isSelected,
                                priceOverride = dynamicPrices[plan],
                                onSelect = { selectedPlan = plan },
                            )
                        }
                    }

                    // ── 4. High-Converting CTA Button ────────────────────────
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick = {
                                onPurchasePlan(selectedPlan)
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    Brush.horizontalGradient(
                                        listOf(Color(0x80FFFFFF), Color(0x30FFFFFF))
                                    ),
                                    RoundedCornerShape(16.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFF5371FF),
                                                Color(0xFF7C3AED),
                                                Color(0xFF06B6D4),
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        imageVector = ObsidianIcons.Sparkles,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = GamingGold,
                                    )
                                    Text(
                                        text = when (selectedPlan) {
                                            SubscriptionPlan.ANNUAL   -> "Start 7-Day Free Trial"
                                            SubscriptionPlan.LIFETIME -> "Unlock Lifetime VIP Access"
                                            SubscriptionPlan.MONTHLY  -> "Start Monthly Membership"
                                        },
                                        color = GamingTextPrimary,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                    )
                                }
                            }
                        }

                        // Reassurance Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Icon(
                                imageVector = ObsidianIcons.Check,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = GamingEmerald,
                            )
                            Text(
                                text = if (selectedPlan.hasTrial) {
                                    "No charge for 7 days • Cancel anytime in Google Play"
                                } else {
                                    "Instant access • Powered by RevenueCat & Google Play"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = GamingTextSecondary,
                                fontSize = 11.5.sp,
                                textAlign = TextAlign.Center,
                            )
                        }

                        // ── Dedicated Promo Code Button ──────────────────────────
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1800E5FF))
                                .border(1.dp, Color(0x3500E5FF), RoundedCornerShape(12.dp))
                                .clickable { showPromoDialog = true }
                                .padding(vertical = 10.dp, horizontal = 14.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(7.dp),
                            ) {
                                Text("🎟️", fontSize = 13.sp)
                                Text(
                                    text = "Redeem Promo Code",
                                    color = GamingCyan,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }

                    // ── 5. Trust & Footer Links ──────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Restore Purchases",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingTextSecondary,
                            modifier = Modifier
                                .clickable {
                                    onRestorePurchases()
                                    onDismiss()
                                }
                                .padding(vertical = 4.dp, horizontal = 6.dp),
                            fontWeight = FontWeight.Medium,
                        )
                        Text("•", color = GamingTextMuted, modifier = Modifier.padding(horizontal = 2.dp))
                        Text(
                            text = "Terms of Service",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingTextMuted,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                        )
                        Text("•", color = GamingTextMuted, modifier = Modifier.padding(horizontal = 2.dp))
                        Text(
                            text = "Privacy Policy",
                            style = MaterialTheme.typography.labelSmall,
                            color = GamingTextMuted,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                        )
                    }
                }

                // Promo Code Overlay
                if (showPromoDialog) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0xEB090D1A))
                            .zIndex(30f)
                            .clickable(enabled = false) {},
                        contentAlignment = Alignment.Center,
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(0.92f)
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
                                androidx.compose.material3.OutlinedTextField(
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
                                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
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
                                    androidx.compose.material3.OutlinedButton(
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
                                                onToggleJudgePro(true)
                                                showPromoDialog = false
                                                onDismiss()
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

                // Top-Right Close Button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                        .zIndex(10f)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(GamingStat.copy(alpha = 0.85f))
                        .border(1.dp, Color(0x25FFFFFF), CircleShape)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = ObsidianIcons.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(13.dp),
                        tint = GamingTextSecondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun PaywallHeroHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // ── Holographic Obsidian Diamond Shard with Game Remote Controller ──
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center,
        ) {
            // Rotated Diamond Crystal Shard (matching Splash Screen)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .rotate(45f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DialogSurfaceGradient)
                    .border(1.5.dp, DialogBorderGlowBrush, RoundedCornerShape(12.dp)),
            )

            // Glowing Neon Cyan Game Remote Controller
            Icon(
                imageVector = ObsidianIcons.Gamepad,
                contentDescription = "ObsidianPlay",
                modifier = Modifier.size(28.dp),
                tint = GamingCyan,
            )
        }

        // Pill Tag
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x308B5CF6))
                .border(1.dp, Color(0x608B5CF6), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 3.dp),
        ) {
            Text(
                text = "⚡ PRESTIGE GAMER MEMBERSHIP",
                color = GamingViolet,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 9.5.sp,
                letterSpacing = 1.sp,
            )
        }

        // Branded Multi-Color Wordmark with PRO Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = GamingTextPrimary, fontWeight = FontWeight.Bold)) {
                        append("Obsidian")
                    }
                    withStyle(SpanStyle(color = GamingBlue, fontWeight = FontWeight.ExtraBold)) {
                        append("Play")
                    }
                },
                fontSize = 24.sp,
                letterSpacing = (-0.5).sp,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFFFB800), Color(0xFFFF7700))
                        )
                    )
                    .padding(horizontal = 7.dp, vertical = 2.5.dp),
            ) {
                Text(
                    text = "PRO VIP",
                    color = Color(0xFF1E1000),
                    fontWeight = FontWeight.Black,
                    fontSize = 10.5.sp,
                    letterSpacing = 0.5.sp,
                )
            }
        }

        Text(
            text = "Elevate your game discovery, generate 9:16 viral cards & level up twice as fast.",
            style = MaterialTheme.typography.bodySmall,
            color = GamingTextSecondary,
            textAlign = TextAlign.Center,
            fontSize = 12.5.sp,
            lineHeight = 17.sp,
            modifier = Modifier.padding(horizontal = 10.dp),
        )
    }
}

@Composable
private fun ProFeaturesShowcase() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard.copy(alpha = 0.9f)),
        border = BorderStroke(1.dp, Color(0x25FFFFFF)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp),
        ) {
            FeatureHighlightRow(
                icon = ObsidianIcons.Sparkles,
                iconTint = GamingViolet,
                iconBg = Color(0x258B5CF6),
                title = "9:16 Viral Story Themes",
                desc = "Unlock Neon Emerald, Sunset Cyber & 2026 Wrapped cards",
            )
            FeatureHighlightRow(
                icon = ObsidianIcons.Dice,
                iconTint = GamingCyan,
                iconBg = Color(0x2506B6D4),
                title = "AI Backlog Roulette",
                desc = "Instant game picker tailored to mood & Steam Deck",
            )
            FeatureHighlightRow(
                icon = ObsidianIcons.Trophy,
                iconTint = GamingGold,
                iconBg = Color(0x25FFB800),
                title = "2X Conquest XP & DNA Badge",
                desc = "Double leveling speed + exclusive VIP golden badge",
            )
            FeatureHighlightRow(
                icon = ObsidianIcons.Link,
                iconTint = GamingRed,
                iconBg = Color(0x25FF3366),
                title = "TikTok & Instagram Video Link Sync",
                desc = "Instant game detection from reels into your backlog",
            )
            FeatureHighlightRow(
                icon = ObsidianIcons.Chart,
                iconTint = GamingEmerald,
                iconBg = Color(0x2510B981),
                title = "Gamer DNA Analytics & Cloud Sync",
                desc = "Cost-per-hour metrics & cross-device library backup",
            )
        }
    }
}

@Composable
private fun FeatureHighlightRow(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    desc: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg)
                .border(1.dp, iconTint.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = iconTint,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(1.5.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = GamingTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall,
                color = GamingTextSecondary,
                fontSize = 11.sp,
                lineHeight = 14.sp,
            )
        }
    }
}

@Composable
private fun ProPlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    priceOverride: String?,
    onSelect: () -> Unit,
) {
    val animatedBg by animateColorAsState(
        targetValue = if (isSelected) GamingCardElevated else GamingCard.copy(alpha = 0.7f),
        animationSpec = spring(stiffness = 400f),
    )

    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = animatedBg),
        border = BorderStroke(
            if (isSelected) 1.6.dp else 1.dp,
            if (isSelected) {
                Brush.horizontalGradient(
                    listOf(Color(0xFF5371FF), Color(0xFF8B5CF6), Color(0xFF06B6D4))
                )
            } else {
                Brush.horizontalGradient(
                    listOf(Color(0x18FFFFFF), Color(0x10FFFFFF))
                )
            }
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f).padding(end = 8.dp),
            ) {
                // Radio Selector
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) GamingBlue else Color.Transparent)
                        .border(
                            1.5.dp,
                            if (isSelected) GamingBlue else GamingTextMuted,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = plan.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GamingTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            maxLines = 1,
                        )
                        if (plan.isBestValue) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF10B981), Color(0xFF06B6D4))
                                        )
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                            ) {
                                Text(
                                    text = "SAVE 45%",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.sp,
                                    letterSpacing = 0.5.sp,
                                )
                            }
                        }
                    }
                    Text(
                        text = plan.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1,
                    )
                }
            }

            // Price Section
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                Text(
                    text = priceOverride ?: plan.priceDisplay,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GamingTextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.5.sp,
                    maxLines = 1,
                )
                Text(
                    text = plan.periodDisplay,
                    style = MaterialTheme.typography.labelSmall,
                    color = GamingTextMuted,
                    fontSize = 10.sp,
                    maxLines = 1,
                )
            }
        }
    }
}


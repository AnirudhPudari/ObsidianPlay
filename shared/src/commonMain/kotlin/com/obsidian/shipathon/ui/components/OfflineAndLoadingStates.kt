package com.obsidian.shipathon.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidian.shipathon.ui.theme.ElectricGradient
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

@Composable
fun HomeSkeletonFeed() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingBackgroundNebula),
        contentPadding = PaddingValues(top = 8.dp, bottom = 36.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Shimmer Hero Spotlight Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    cornerRadius = 24.dp,
                )
            }
        }

        // Shimmer Genre Filter Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(6) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(80.dp)
                            .height(34.dp),
                        cornerRadius = 10.dp,
                    )
                }
            }
        }

        // Shimmer Section 1: Trending Now
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(140.dp)
                            .height(22.dp),
                        cornerRadius = 6.dp,
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .width(50.dp)
                            .height(18.dp),
                        cornerRadius = 6.dp,
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(4) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .width(135.dp)
                                    .height(190.dp),
                                cornerRadius = 16.dp,
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(14.dp),
                                cornerRadius = 4.dp,
                            )
                        }
                    }
                }
            }
        }

        // Shimmer Section 2: Top Rated
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .width(160.dp)
                        .height(22.dp),
                    cornerRadius = 6.dp,
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(4) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(135.dp)
                                .height(190.dp),
                            cornerRadius = 16.dp,
                        )
                    }
                }
            }
        }
    }
}

// ── Cyberpunk Offline & No Internet Recovery UI ─────────────────────────────

@Composable
fun CyberpunkOfflineState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "PulseRadar",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GamingBackgroundNebula)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = GamingCard),
            border = BorderStroke(1.2.dp, GamingRed.copy(alpha = 0.5f)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Pulsing Holographic Radar Beacon Icon
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(GamingRed.copy(alpha = 0.15f))
                        .border(1.5.dp, GamingRed.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("📡", fontSize = 32.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "RADAR OFFLINE",
                        style = MaterialTheme.typography.titleLarge,
                        color = GamingTextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                    )
                    Text(
                        text = "No Internet Connection Detected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GamingRed,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Text(
                    text = "Could not connect to online game servers. Don't worry, all saved games in your Library are safe and 100% accessible offline!",
                    style = MaterialTheme.typography.bodySmall,
                    color = GamingTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                )

                // Local Database Cache Safe Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(GamingEmerald.copy(alpha = 0.15f))
                        .border(1.dp, GamingEmerald.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("🛡️", fontSize = 12.sp)
                    Text(
                        text = "Offline Local Vault Active",
                        style = MaterialTheme.typography.labelSmall,
                        color = GamingEmerald,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Reconnect CTA Button
                Button(
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GamingBlue),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("⚡", fontSize = 16.sp)
                        Text(
                            text = "Reconnect Radar",
                            color = GamingTextPrimary,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

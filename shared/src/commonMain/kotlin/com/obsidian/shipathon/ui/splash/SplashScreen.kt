package com.obsidian.shipathon.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidian.shipathon.ui.theme.DialogBorderGlowBrush
import com.obsidian.shipathon.ui.theme.DialogSurfaceGradient
import com.obsidian.shipathon.ui.theme.GamingBackgroundNebula
import com.obsidian.shipathon.ui.theme.GamingBlue
import com.obsidian.shipathon.ui.theme.GamingCyan
import com.obsidian.shipathon.ui.theme.GamingStat
import com.obsidian.shipathon.ui.theme.GamingTextPrimary
import com.obsidian.shipathon.ui.theme.GamingTextSecondary
import com.obsidian.shipathon.ui.theme.GamingViolet
import com.obsidian.shipathon.ui.theme.GlassBorderBrush
import com.obsidian.shipathon.ui.theme.ObsidianIcons

@Composable
fun SplashScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "SplashPulse")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "PulseScale",
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.40f,
        targetValue = 0.90f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "GlowAlpha",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingBackgroundNebula),
        contentAlignment = Alignment.Center,
    ) {
        // Atmospheric Obsidian Ambient Nebula Glow behind logo
        Box(
            modifier = Modifier
                .size(260.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            GamingBlue.copy(alpha = glowAlpha * 0.45f),
                            GamingViolet.copy(alpha = glowAlpha * 0.25f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // ── Holographic Obsidian Diamond Shard (1:1 with Vector Launcher Icon) ──
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center,
            ) {
                // Rotated Diamond Crystal Shard
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .rotate(45f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(DialogSurfaceGradient)
                        .border(1.5.dp, DialogBorderGlowBrush, RoundedCornerShape(14.dp)),
                )

                // Glowing Neon Cyan D-Pad + Play Controller Core
                Icon(
                    imageVector = ObsidianIcons.Gamepad,
                    contentDescription = "ObsidianPlay",
                    modifier = Modifier.size(38.dp),
                    tint = GamingCyan,
                )
            }

            Spacer(Modifier.height(28.dp))

            // Branded Wordmark
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = GamingTextPrimary, fontWeight = FontWeight.Bold)) {
                        append("Obsidian")
                    }
                    withStyle(SpanStyle(color = GamingBlue, fontWeight = FontWeight.ExtraBold)) {
                        append("Play")
                    }
                },
                fontSize = 32.sp,
                letterSpacing = (-0.8).sp,
            )

            Spacer(Modifier.height(6.dp))

            // Subtitle / Tagline
            Text(
                text = "Your Gaming Bucket List, Conquered",
                style = MaterialTheme.typography.bodyMedium,
                color = GamingTextSecondary,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.2.sp,
            )

            Spacer(Modifier.height(48.dp))

            // Micro-loader / Sync Indicator
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(GamingStat.copy(alpha = 0.85f))
                    .border(1.dp, GlassBorderBrush, RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(GamingCyan)
                )
                Text(
                    text = "Entering Gaming Universe...",
                    style = MaterialTheme.typography.labelSmall,
                    color = GamingCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        // Bottom Footer
        Text(
            text = "OBSIDIANPLAY • YOUR GAMING UNIVERSE",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.4.sp),
            color = GamingTextSecondary.copy(alpha = 0.5f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

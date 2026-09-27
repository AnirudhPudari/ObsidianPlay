package com.obsidian.shipathon.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ── Obsidian Cyber-Glass Atmospheric Palette ─────────────────────────────────
// Elevated Atmospheric Glass Surfaces (Deep Midnight Navy with Specular Sheen)
val GamingBackground        = Color(0xFF090D1A)  // Deep Midnight Navy (Richer than flat black)
val GamingCard              = Color(0xFF131B2E)  // Frosted Deep Glass Surface (Clear Contrast)
val GamingCardElevated      = Color(0xFF1C2742)  // Elevated Dialog & Sheet Glass Surface
val GamingStat              = Color(0xFF202E4C)  // Micro Pill & Stat Surface
val GamingBorder            = Color(0x35FFFFFF)  // Specular Frosted Glass Border (21% Translucent)

// Atmospheric Background Nebula Gradients
val GamingBackgroundNebula  = Brush.verticalGradient(
    listOf(
        Color(0xFF141D38), // Soft glowing atmospheric header
        Color(0xFF0D1224),
        Color(0xFF080C17), // Deep bottom anchor
    )
)

val HeroAmbientGlow         = Brush.radialGradient(
    colors = listOf(
        Color(0x555371FF),
        Color(0x307C3AED),
        Color.Transparent,
    )
)

// Vibrant Neon & Electric Accents
val GamingBlue              = Color(0xFF5371FF)  // Electric Cobalt / Primary CTA
val GamingBlueDim           = Color(0xFF384ECC)  // Deep Indigo / Ripple
val GamingViolet            = Color(0xFF8B5CF6)  // Cyber Violet / Specular Accent
val GamingCyan              = Color(0xFF06B6D4)  // Neon Cyan / Sparkle
val GamingGold              = Color(0xFFFFB800)  // Metacritic Trophy Gold
val GamingEmerald           = Color(0xFF10B981)  // Masterpiece Emerald Green
val GamingRed               = Color(0xFFFF3366)  // Flame Hot Coral / Accent Badge

// Typography
val GamingTextPrimary       = Color(0xFFFFFFFF)  // Pure Crisp White (Maximum Pop)
val GamingTextSecondary     = Color(0xFFA0AEC0)  // Light Silver Slate (High Readability)
val GamingTextMuted         = Color(0xFF64748B)  // Muted Caption Slate

// Genre Chips
val GamingChip              = Color(0xFF192238)
val GamingChipHorror        = Color(0xFF331624)

// ── Reusable Premium Gradients ────────────────────────────────────────────────
val ElectricGradient        = Brush.horizontalGradient(listOf(Color(0xFF4F46E5), Color(0xFF6366F1)))
val GoldGradient            = Brush.horizontalGradient(listOf(Color(0xFFFFB800), Color(0xFFFF7700)))
val CyanGradient            = Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFF3B82F6)))
val VioletGradient          = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFEC4899)))

// Glassmorphism Lighting
val GlassBorderBrush        = Brush.verticalGradient(
    listOf(
        Color(0x55FFFFFF), // Crisp top light edge
        Color(0x15FFFFFF), // Soft bottom edge
    )
)

val CardGlowBorderBrush     = Brush.linearGradient(
    listOf(
        Color(0x605371FF),
        Color(0x4000E5FF),
        Color(0x20FFFFFF),
    )
)

val HeaderGlowBrush         = Brush.horizontalGradient(
    listOf(
        Color.Transparent,
        Color(0x805371FF),
        Color(0x807C3AED),
        Color.Transparent,
    )
)

val CardBackdropOverlay     = Brush.verticalGradient(
    listOf(
        Color.Transparent,
        Color(0x66131B2E),
        Color(0xF0131B2E),
    )
)

// Dialogs & Sheets (Refined Native Glass Surfaces)
val DialogSurfaceGradient   = Brush.verticalGradient(
    listOf(
        Color(0xFF141C30),
        Color(0xFF0B1020),
    )
)

val DialogBorderGlowBrush   = Brush.verticalGradient(
    listOf(
        Color(0x35FFFFFF), // Crisp top hairline
        Color(0x10FFFFFF), // Subtle bottom anchor
    )
)

// Social Source Gradients
val TikTokGradient          = Brush.horizontalGradient(listOf(Color(0xFFFF0050), Color(0xFF00F2FE)))
val InstagramGradient       = Brush.horizontalGradient(listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045)))
val YouTubeGradient         = Brush.horizontalGradient(listOf(Color(0xFFFF0000), Color(0xFFCC0000)))
val TwitchGradient          = Brush.horizontalGradient(listOf(Color(0xFF9146FF), Color(0xFF6441A5)))
val RedditGradient          = Brush.horizontalGradient(listOf(Color(0xFFFF4500), Color(0xFFFF5722)))
val TwitterGradient         = Brush.horizontalGradient(listOf(Color(0xFF1DA1F2), Color(0xFF0D8ECF)))
val SteamGradient           = Brush.horizontalGradient(listOf(Color(0xFF171A21), Color(0xFF1B2838), Color(0xFF2A475E)))

// Share Card Presets
val CyberObsidianPreset     = Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF020617)))
val NeonEmeraldPreset       = Brush.verticalGradient(listOf(Color(0xFF065F46), Color(0xFF064E3B), Color(0xFF022C22)))
val SunsetFlamePreset       = Brush.verticalGradient(listOf(Color(0xFF9D174D), Color(0xFF831843), Color(0xFF4C0519)))

// Fallback Light Palette
val LightPrimary            = Color(0xFF5371FF)
val LightBackground         = Color(0xFFF4F6FB)
val LightSurface            = Color(0xFFFFFFFF)
val LightOnSurface          = Color(0xFF07090E)
val LightSecondary          = Color(0xFF64748B)

package com.obsidian.shipathon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary             = GamingBlue,
    onPrimary           = GamingTextPrimary,
    primaryContainer    = GamingStat,
    onPrimaryContainer  = GamingTextPrimary,
    secondary           = GamingTextSecondary,
    onSecondary         = GamingTextPrimary,
    secondaryContainer  = GamingChip,
    onSecondaryContainer= GamingTextPrimary,
    background          = GamingBackground,
    onBackground        = GamingTextPrimary,
    surface             = GamingCard,
    onSurface           = GamingTextPrimary,
    surfaceVariant      = GamingStat,
    onSurfaceVariant    = GamingTextSecondary,
    error               = GamingRed,
    onError             = GamingTextPrimary,
)

private val LightColors = lightColorScheme(
    primary             = LightPrimary,
    background          = LightBackground,
    surface             = LightSurface,
    onBackground        = LightOnSurface,
    onSurface           = LightOnSurface,
    secondary           = LightSecondary,
    onSecondary         = LightSurface,
)

/**
 * ObsidianPlay theme — defaults to dark to match the gaming design.
 * Pass [darkTheme] = false to force the light variant.
 */
@Composable
fun ObsidianPlayTheme(
    darkTheme: Boolean = true,  // gaming app is dark-first
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = getObsidianTypography(),
        content = content,
    )
}

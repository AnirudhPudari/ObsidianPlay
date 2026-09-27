package com.obsidian.shipathon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import obsidianplay.shared.generated.resources.Res
import obsidianplay.shared.generated.resources.plus_jakarta_sans_bold
import obsidianplay.shared.generated.resources.plus_jakarta_sans_extrabold
import obsidianplay.shared.generated.resources.plus_jakarta_sans_medium
import obsidianplay.shared.generated.resources.plus_jakarta_sans_regular
import obsidianplay.shared.generated.resources.plus_jakarta_sans_semibold

@Composable
fun getObsidianFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.plus_jakarta_sans_regular, weight = FontWeight.Normal),
        Font(Res.font.plus_jakarta_sans_medium, weight = FontWeight.Medium),
        Font(Res.font.plus_jakarta_sans_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.plus_jakarta_sans_bold, weight = FontWeight.Bold),
        Font(Res.font.plus_jakarta_sans_extrabold, weight = FontWeight.ExtraBold),
    )
}

@Composable
fun getObsidianTypography(): Typography {
    val fontFamily = getObsidianFontFamily()
    return createTypography(fontFamily)
}

private fun createTypography(fontFamily: FontFamily): Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.8).sp,
        color = GamingTextPrimary,
    ),
    headlineMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.5).sp,
        color = GamingTextPrimary,
    ),
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.3).sp,
        color = GamingTextPrimary,
    ),
    titleMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp,
        color = GamingTextPrimary,
    ),
    titleSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        color = GamingTextPrimary,
    ),
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp,
        color = GamingTextPrimary,
    ),
    bodyMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.1.sp,
        color = GamingTextSecondary,
    ),
    bodySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        color = GamingTextSecondary,
    ),
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
        color = GamingTextPrimary,
    ),
    labelMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        color = GamingTextSecondary,
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.0.sp,
        color = GamingTextSecondary,
    ),
)

val ObsidianTypography: Typography = createTypography(FontFamily.SansSerif)


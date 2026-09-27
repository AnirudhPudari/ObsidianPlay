package com.obsidian.shipathon.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * ObsidianPlay Custom Console-Grade Vector Icon Suite.
 * Clean, geometric, high-precision vector paths designed for modern dark gaming interfaces.
 */
object ObsidianIcons {

    // ── 1. Home Icon ─────────────────────────────────────────────────────────
    val Home: ImageVector = ImageVector.Builder(
        name = "Home",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(12f, 3f)
            lineTo(3.5f, 9.8f)
            curveTo(3.18f, 10.05f, 3f, 10.45f, 3f, 10.85f)
            verticalLineTo(19.5f)
            curveTo(3f, 20.6f, 3.9f, 21.5f, 5f, 21.5f)
            horizontalLineTo(9.5f)
            verticalLineTo(15f)
            curveTo(9.5f, 14.17f, 10.17f, 13.5f, 11f, 13.5f)
            horizontalLineTo(13f)
            curveTo(13.83f, 13.5f, 14.5f, 14.17f, 14.5f, 15f)
            verticalLineTo(21.5f)
            horizontalLineTo(19f)
            curveTo(20.1f, 21.5f, 21f, 20.6f, 21f, 19.5f)
            verticalLineTo(10.85f)
            curveTo(21f, 10.45f, 20.82f, 10.05f, 20.5f, 9.8f)
            lineTo(12f, 3f)
            close()
        }
    }.build()

    // ── 2. Gamepad / Controller Icon ─────────────────────────────────────────
    val Gamepad: ImageVector = ImageVector.Builder(
        name = "Gamepad",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(21f, 6f)
            horizontalLineTo(3f)
            curveTo(1.34f, 6f, 0f, 7.34f, 0f, 9f)
            verticalLineTo(15f)
            curveTo(0f, 17.76f, 2.24f, 20f, 5f, 20f)
            curveTo(6.85f, 20f, 8.45f, 18.99f, 9.3f, 17.5f)
            lineTo(11.2f, 14.5f)
            horizontalLineTo(12.8f)
            lineTo(14.7f, 17.5f)
            curveTo(15.55f, 18.99f, 17.15f, 20f, 19f, 20f)
            curveTo(21.76f, 20f, 24f, 17.76f, 24f, 15f)
            verticalLineTo(9f)
            curveTo(24f, 7.34f, 22.66f, 6f, 21f, 6f)
            close()
            // D-Pad
            moveTo(8f, 11f)
            horizontalLineTo(6.5f)
            verticalLineTo(9.5f)
            horizontalLineTo(5.5f)
            verticalLineTo(11f)
            horizontalLineTo(4f)
            verticalLineTo(12f)
            horizontalLineTo(5.5f)
            verticalLineTo(13.5f)
            horizontalLineTo(6.5f)
            verticalLineTo(12f)
            horizontalLineTo(8f)
            verticalLineTo(11f)
            close()
            // Action buttons
            moveTo(18f, 10f)
            curveTo(18.55f, 10f, 19f, 10.45f, 19f, 11f)
            curveTo(19f, 11.55f, 18.55f, 12f, 18f, 12f)
            curveTo(17.45f, 12f, 17f, 11.55f, 17f, 11f)
            curveTo(17f, 10.45f, 17.45f, 10f, 18f, 10f)
            close()
            moveTo(20f, 12f)
            curveTo(20.55f, 12f, 21f, 12.45f, 21f, 13f)
            curveTo(21f, 13.55f, 20.55f, 14f, 20f, 14f)
            curveTo(19.45f, 14f, 19f, 13.55f, 19f, 13f)
            curveTo(19f, 12.45f, 19.45f, 12f, 20f, 12f)
            close()
            moveTo(16f, 12f)
            curveTo(16.55f, 12f, 17f, 12.45f, 17f, 13f)
            curveTo(17f, 13.55f, 16.55f, 14f, 16f, 14f)
            curveTo(15.45f, 14f, 15f, 13.55f, 15f, 13f)
            curveTo(15f, 12.45f, 15.45f, 12f, 16f, 12f)
            close()
            moveTo(18f, 14f)
            curveTo(18.55f, 14f, 19f, 14.45f, 19f, 15f)
            curveTo(19f, 15.55f, 18.55f, 16f, 18f, 16f)
            curveTo(17.45f, 16f, 17f, 15.55f, 17f, 15f)
            curveTo(17f, 14.45f, 17.45f, 14f, 18f, 14f)
            close()
        }
    }.build()

    // ── 3. Search Lens Icon ──────────────────────────────────────────────────
    val Search: ImageVector = ImageVector.Builder(
        name = "Search",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(10f, 2f)
            curveTo(5.58f, 2f, 2f, 5.58f, 2f, 10f)
            curveTo(2f, 14.42f, 5.58f, 18f, 10f, 18f)
            curveTo(11.85f, 18f, 13.55f, 17.37f, 14.9f, 16.31f)
            lineTo(20.29f, 21.71f)
            curveTo(20.68f, 22.1f, 21.32f, 22.1f, 21.71f, 21.71f)
            curveTo(22.1f, 21.32f, 22.1f, 20.68f, 21.71f, 20.29f)
            lineTo(16.31f, 14.9f)
            curveTo(17.37f, 13.55f, 18f, 11.85f, 18f, 10f)
            curveTo(18f, 5.58f, 14.42f, 2f, 10f, 2f)
            close()
            moveTo(10f, 4f)
            curveTo(13.31f, 4f, 16f, 6.69f, 16f, 10f)
            curveTo(16f, 13.31f, 13.31f, 16f, 10f, 16f)
            curveTo(6.69f, 16f, 4f, 13.31f, 4f, 10f)
            curveTo(4f, 6.69f, 6.69f, 4f, 10f, 4f)
            close()
        }
    }.build()

    // ── 4. Profile / Gamer User Icon ─────────────────────────────────────────
    val Profile: ImageVector = ImageVector.Builder(
        name = "Profile",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(12f, 2f)
            curveTo(8.69f, 2f, 6f, 4.69f, 6f, 8f)
            curveTo(6f, 11.31f, 8.69f, 14f, 12f, 14f)
            curveTo(15.31f, 14f, 18f, 11.31f, 18f, 8f)
            curveTo(18f, 4.69f, 15.31f, 2f, 12f, 2f)
            close()
            moveTo(12f, 16f)
            curveTo(7.58f, 16f, 2f, 18.24f, 2f, 21f)
            curveTo(2f, 21.55f, 2.45f, 22f, 3f, 22f)
            horizontalLineTo(21f)
            curveTo(21.55f, 22f, 22f, 21.55f, 22f, 21f)
            curveTo(22f, 18.24f, 16.42f, 16f, 12f, 16f)
            close()
        }
    }.build()

    // ── 5. Trophy Icon ───────────────────────────────────────────────────────
    val Trophy: ImageVector = ImageVector.Builder(
        name = "Trophy",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFFFFB800)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(19f, 3f)
            horizontalLineTo(17f)
            verticalLineTo(2f)
            horizontalLineTo(7f)
            verticalLineTo(3f)
            horizontalLineTo(5f)
            curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
            verticalLineTo(8f)
            curveTo(3f, 10.21f, 4.79f, 12f, 7f, 12f)
            horizontalLineTo(7.1f)
            curveTo(7.85f, 13.79f, 9.47f, 15.15f, 11.5f, 15.45f)
            verticalLineTo(18f)
            horizontalLineTo(8f)
            verticalLineTo(20f)
            horizontalLineTo(16f)
            verticalLineTo(18f)
            horizontalLineTo(12.5f)
            verticalLineTo(15.45f)
            curveTo(14.53f, 15.15f, 16.15f, 13.79f, 16.9f, 12f)
            horizontalLineTo(17f)
            curveTo(19.21f, 12f, 21f, 10.21f, 21f, 8f)
            verticalLineTo(5f)
            curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
            close()
            moveTo(5f, 8f)
            verticalLineTo(5f)
            horizontalLineTo(7f)
            verticalLineTo(10f)
            curveTo(5.9f, 10f, 5f, 9.1f, 5f, 8f)
            close()
            moveTo(19f, 8f)
            curveTo(19f, 9.1f, 18.1f, 10f, 17f, 10f)
            verticalLineTo(5f)
            horizontalLineTo(19f)
            verticalLineTo(8f)
            close()
        }
    }.build()

    // ── 6. Dice / Roulette Icon ──────────────────────────────────────────────
    val Dice: ImageVector = ImageVector.Builder(
        name = "Dice",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(19f, 3f)
            horizontalLineTo(5f)
            curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
            verticalLineTo(19f)
            curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
            horizontalLineTo(19f)
            curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
            verticalLineTo(5f)
            curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
            close()
            // Dots
            moveTo(7.5f, 7.5f)
            curveTo(8.05f, 7.5f, 8.5f, 7.95f, 8.5f, 8.5f)
            curveTo(8.5f, 9.05f, 8.05f, 9.5f, 7.5f, 9.5f)
            curveTo(6.95f, 9.5f, 6.5f, 9.05f, 6.5f, 8.5f)
            curveTo(6.5f, 7.95f, 6.95f, 7.5f, 7.5f, 7.5f)
            close()
            moveTo(12f, 12f)
            curveTo(12.55f, 12f, 13f, 12.45f, 13f, 13f)
            curveTo(13f, 13.55f, 12.55f, 14f, 12f, 14f)
            curveTo(11.45f, 14f, 11f, 13.55f, 11f, 13f)
            curveTo(11f, 12.45f, 11.45f, 12f, 12f, 12f)
            close()
            moveTo(16.5f, 16.5f)
            curveTo(17.05f, 16.5f, 17.5f, 16.95f, 17.5f, 17.5f)
            curveTo(17.5f, 18.05f, 17.05f, 18.5f, 16.5f, 18.5f)
            curveTo(15.95f, 18.5f, 15.5f, 18.05f, 15.5f, 17.5f)
            curveTo(15.5f, 16.95f, 15.95f, 16.5f, 16.5f, 16.5f)
            close()
        }
    }.build()

    // ── 7. Link / Share Icon ─────────────────────────────────────────────────
    val Link: ImageVector = ImageVector.Builder(
        name = "Link",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(3.9f, 12f)
            curveTo(3.9f, 10.29f, 5.29f, 8.9f, 7f, 8.9f)
            horizontalLineTo(11f)
            verticalLineTo(7f)
            horizontalLineTo(7f)
            curveTo(4.24f, 7f, 2f, 9.24f, 2f, 12f)
            curveTo(2f, 14.76f, 4.24f, 17f, 7f, 17f)
            horizontalLineTo(11f)
            verticalLineTo(15.1f)
            horizontalLineTo(7f)
            curveTo(5.29f, 15.1f, 3.9f, 13.71f, 3.9f, 12f)
            close()
            moveTo(8f, 13f)
            horizontalLineTo(16f)
            verticalLineTo(11f)
            horizontalLineTo(8f)
            verticalLineTo(13f)
            close()
            moveTo(17f, 7f)
            horizontalLineTo(13f)
            verticalLineTo(8.9f)
            horizontalLineTo(17f)
            curveTo(18.71f, 8.9f, 20.1f, 10.29f, 20.1f, 12f)
            curveTo(20.1f, 13.71f, 18.71f, 15.1f, 17f, 15.1f)
            horizontalLineTo(13f)
            verticalLineTo(17f)
            horizontalLineTo(17f)
            curveTo(19.76f, 17f, 22f, 14.76f, 22f, 12f)
            curveTo(22f, 9.24f, 19.76f, 7f, 17f, 7f)
            close()
        }
    }.build()

    // ── 8. Fire / Trending Icon ──────────────────────────────────────────────
    val Fire: ImageVector = ImageVector.Builder(
        name = "Fire",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFFFF3366)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(13.5f, 0.67f)
            curveTo(13.5f, 0.67f, 14.24f, 3.32f, 14.24f, 5.47f)
            curveTo(14.24f, 7.53f, 12.89f, 9.2f, 11.27f, 9.2f)
            curveTo(9.65f, 9.2f, 8.8f, 7.85f, 8.8f, 6.18f)
            curveTo(6.34f, 8.78f, 5f, 12.18f, 5f, 15.65f)
            curveTo(5f, 19.52f, 8.13f, 22.65f, 12f, 22.65f)
            curveTo(15.87f, 22.65f, 19f, 19.52f, 19f, 15.65f)
            curveTo(19f, 10.02f, 15.66f, 4.41f, 13.5f, 0.67f)
            close()
            moveTo(12f, 20.5f)
            curveTo(9.51f, 20.5f, 7.5f, 18.49f, 7.5f, 16f)
            curveTo(7.5f, 14.49f, 8.24f, 12.43f, 9.53f, 11.02f)
            curveTo(10.15f, 12.44f, 11.45f, 13.5f, 13f, 13.5f)
            curveTo(14.93f, 13.5f, 16.5f, 11.93f, 16.5f, 10f)
            curveTo(16.5f, 10.06f, 16.5f, 10.13f, 16.5f, 10.19f)
            curveTo(16.82f, 11.91f, 17f, 13.73f, 17f, 15.65f)
            curveTo(17f, 18.49f, 14.49f, 20.5f, 12f, 20.5f)
            close()
        }
    }.build()

    // ── 9. Sparkles Icon ─────────────────────────────────────────────────────
    val Sparkles: ImageVector = ImageVector.Builder(
        name = "Sparkles",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFF00E5FF)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(12f, 2f)
            lineTo(13.8f, 7.2f)
            lineTo(19f, 9f)
            lineTo(13.8f, 10.8f)
            lineTo(12f, 16f)
            lineTo(10.2f, 10.8f)
            lineTo(5f, 9f)
            lineTo(10.2f, 7.2f)
            lineTo(12f, 2f)
            close()
            moveTo(19f, 15f)
            lineTo(20f, 17.5f)
            lineTo(22.5f, 18.5f)
            lineTo(20f, 19.5f)
            lineTo(19f, 22f)
            lineTo(18f, 19.5f)
            lineTo(15.5f, 18.5f)
            lineTo(18f, 17.5f)
            lineTo(19f, 15f)
            close()
        }
    }.build()

    // ── 10. Chart / Analytics Icon ───────────────────────────────────────────
    val Chart: ImageVector = ImageVector.Builder(
        name = "Chart",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFF10B981)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(4f, 19f)
            horizontalLineTo(20f)
            curveTo(20.55f, 19f, 21f, 19.45f, 21f, 20f)
            curveTo(21f, 20.55f, 20.55f, 21f, 20f, 21f)
            horizontalLineTo(3f)
            curveTo(2.45f, 21f, 2f, 20.55f, 2f, 20f)
            verticalLineTo(3f)
            curveTo(2f, 2.45f, 2.45f, 2f, 3f, 2f)
            curveTo(3.55f, 2f, 4f, 2.45f, 4f, 3f)
            verticalLineTo(19f)
            close()
            // Bar 1
            moveTo(7f, 17f)
            verticalLineTo(13f)
            curveTo(7f, 12.45f, 7.45f, 12f, 8f, 12f)
            horizontalLineTo(9f)
            curveTo(9.55f, 12f, 10f, 12.45f, 10f, 13f)
            verticalLineTo(17f)
            close()
            // Bar 2
            moveTo(12f, 17f)
            verticalLineTo(9f)
            curveTo(12f, 8.45f, 12.45f, 8f, 13f, 8f)
            horizontalLineTo(14f)
            curveTo(14.55f, 8f, 15f, 8.45f, 15f, 9f)
            verticalLineTo(17f)
            close()
            // Bar 3
            moveTo(17f, 17f)
            verticalLineTo(5f)
            curveTo(17f, 4.45f, 17.45f, 4f, 18f, 4f)
            horizontalLineTo(19f)
            curveTo(19.55f, 4f, 20f, 4.45f, 20f, 5f)
            verticalLineTo(17f)
            close()
        }
    }.build()

    // ── 11. Zap / Lightning Icon ─────────────────────────────────────────────
    val Zap: ImageVector = ImageVector.Builder(
        name = "Zap",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFFFFB800)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(13f, 2f)
            lineTo(3f, 14f)
            horizontalLineTo(11f)
            lineTo(9f, 22f)
            lineTo(21f, 10f)
            horizontalLineTo(13f)
            lineTo(13f, 2f)
            close()
        }
    }.build()

    // ── 12. Close / Cross Icon ───────────────────────────────────────────────
    val Close: ImageVector = ImageVector.Builder(
        name = "Close",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(19f, 6.41f)
            lineTo(17.59f, 5f)
            lineTo(12f, 10.59f)
            lineTo(6.41f, 5f)
            lineTo(5f, 6.41f)
            lineTo(10.59f, 12f)
            lineTo(5f, 17.59f)
            lineTo(6.41f, 19f)
            lineTo(12f, 13.41f)
            lineTo(17.59f, 19f)
            lineTo(19f, 17.59f)
            lineTo(13.41f, 12f)
            lineTo(19f, 6.41f)
            close()
        }
    }.build()

    // ── 13. Check / Tick Icon ────────────────────────────────────────────────
    val Check: ImageVector = ImageVector.Builder(
        name = "Check",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(9f, 16.2f)
            lineTo(4.8f, 12f)
            lineTo(3.4f, 13.4f)
            lineTo(9f, 19f)
            lineTo(21f, 7f)
            lineTo(19.6f, 5.6f)
            lineTo(9f, 16.2f)
            close()
        }
    }.build()

    // ── 14. Star / Rating Filled ─────────────────────────────────────────────
    val Star: ImageVector = ImageVector.Builder(
        name = "Star",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFFFFB800)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(12f, 17.27f)
            lineTo(18.18f, 21f)
            lineTo(16.54f, 13.97f)
            lineTo(22f, 9.24f)
            lineTo(14.81f, 8.63f)
            lineTo(12f, 2f)
            lineTo(9.19f, 8.63f)
            lineTo(2f, 9.24f)
            lineTo(7.46f, 13.97f)
            lineTo(5.82f, 21f)
            lineTo(12f, 17.27f)
            close()
        }
    }.build()

    // ── 15. Star Outline ─────────────────────────────────────────────────────
    val StarOutline: ImageVector = ImageVector.Builder(
        name = "StarOutline",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFF64748B)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(22f, 9.24f)
            lineTo(14.81f, 8.62f)
            lineTo(12f, 2f)
            lineTo(9.19f, 8.63f)
            lineTo(2f, 9.24f)
            lineTo(7.46f, 13.97f)
            lineTo(5.82f, 21f)
            lineTo(12f, 17.27f)
            lineTo(18.18f, 21f)
            lineTo(16.55f, 13.97f)
            lineTo(22f, 9.24f)
            close()
            moveTo(12f, 15.4f)
            lineTo(8.24f, 17.67f)
            lineTo(9.24f, 13.39f)
            lineTo(5.92f, 10.51f)
            lineTo(10.3f, 10.13f)
            lineTo(12f, 6.1f)
            lineTo(13.71f, 10.14f)
            lineTo(18.09f, 10.52f)
            lineTo(14.77f, 13.4f)
            lineTo(15.77f, 17.68f)
            lineTo(12f, 15.4f)
            close()
        }
    }.build()

    // ── 16. Trash / Delete Icon ──────────────────────────────────────────────
    val Trash: ImageVector = ImageVector.Builder(
        name = "Trash",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(6f, 19f)
            curveTo(6f, 20.1f, 6.9f, 21f, 8f, 21f)
            horizontalLineTo(16f)
            curveTo(17.1f, 21f, 18f, 20.1f, 18f, 19f)
            verticalLineTo(7f)
            horizontalLineTo(6f)
            verticalLineTo(19f)
            close()
            moveTo(19f, 4f)
            horizontalLineTo(15.5f)
            lineTo(14.5f, 3f)
            horizontalLineTo(9.5f)
            lineTo(8.5f, 4f)
            horizontalLineTo(5f)
            verticalLineTo(6f)
            horizontalLineTo(19f)
            verticalLineTo(4f)
            close()
        }
    }.build()

    // ── 17. Info Icon ───────────────────────────────────────────────────────
    val Info: ImageVector = ImageVector.Builder(
        name = "Info",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
            curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
            curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
            close()
            moveTo(13f, 17f)
            horizontalLineTo(11f)
            verticalLineTo(11f)
            horizontalLineTo(13f)
            verticalLineTo(17f)
            close()
            moveTo(13f, 9f)
            horizontalLineTo(11f)
            verticalLineTo(7f)
            horizontalLineTo(13f)
            verticalLineTo(9f)
            close()
        }
    }.build()

    // ── 18. Crown / VIP Icon ─────────────────────────────────────────────────
    val Crown: ImageVector = ImageVector.Builder(
        name = "Crown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFFFFB800)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(5f, 16f)
            lineTo(3f, 5f)
            lineTo(8.5f, 10f)
            lineTo(12f, 4f)
            lineTo(15.5f, 10f)
            lineTo(21f, 5f)
            lineTo(19f, 16f)
            horizontalLineTo(5f)
            close()
            moveTo(5f, 18f)
            horizontalLineTo(19f)
            curveTo(19.55f, 18f, 20f, 18.45f, 20f, 19f)
            curveTo(20f, 19.55f, 19.55f, 20f, 19f, 20f)
            horizontalLineTo(5f)
            curveTo(4.45f, 20f, 4f, 19.55f, 4f, 19f)
            curveTo(4f, 18.45f, 4.45f, 18f, 5f, 18f)
            close()
        }
    }.build()

    // ── 19. Ticket / Voucher Icon ────────────────────────────────────────────
    val Ticket: ImageVector = ImageVector.Builder(
        name = "Ticket",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFF00E5FF)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(22f, 10f)
            verticalLineTo(6f)
            curveTo(22f, 4.9f, 21.1f, 4f, 20f, 4f)
            horizontalLineTo(4f)
            curveTo(2.9f, 4f, 2f, 4.9f, 2f, 6f)
            verticalLineTo(10f)
            curveTo(3.1f, 10f, 4f, 10.9f, 4f, 12f)
            curveTo(4f, 13.1f, 3.1f, 14f, 2f, 14f)
            verticalLineTo(18f)
            curveTo(2f, 19.1f, 2.9f, 20f, 4f, 20f)
            horizontalLineTo(20f)
            curveTo(21.1f, 20f, 22f, 19.1f, 22f, 18f)
            verticalLineTo(14f)
            curveTo(20.9f, 14f, 20f, 13.1f, 20f, 12f)
            curveTo(20f, 10.9f, 20.9f, 10f, 22f, 10f)
            close()
            moveTo(13f, 17.5f)
            horizontalLineTo(11f)
            verticalLineTo(15.5f)
            horizontalLineTo(13f)
            verticalLineTo(17.5f)
            close()
            moveTo(13f, 13f)
            horizontalLineTo(11f)
            verticalLineTo(11f)
            horizontalLineTo(13f)
            verticalLineTo(13f)
            close()
            moveTo(13f, 8.5f)
            horizontalLineTo(11f)
            verticalLineTo(6.5f)
            horizontalLineTo(13f)
            verticalLineTo(8.5f)
            close()
        }
    }.build()

    // ── 20. DNA / Taste Profile Icon ─────────────────────────────────────────
    val Dna: ImageVector = ImageVector.Builder(
        name = "Dna",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFF00E5FF)),
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(12f, 3f)
            curveTo(10.34f, 3f, 9f, 4.34f, 9f, 6f)
            curveTo(9f, 7.35f, 9.9f, 8.49f, 11.14f, 8.85f)
            lineTo(8.85f, 11.14f)
            curveTo(8.49f, 9.9f, 7.35f, 9f, 6f, 9f)
            curveTo(4.34f, 9f, 3f, 10.34f, 3f, 12f)
            curveTo(3f, 13.66f, 4.34f, 15f, 6f, 15f)
            curveTo(7.35f, 15f, 8.49f, 14.1f, 8.85f, 12.86f)
            lineTo(11.14f, 15.15f)
            curveTo(9.9f, 15.51f, 9f, 16.65f, 9f, 18f)
            curveTo(9f, 19.66f, 10.34f, 21f, 12f, 21f)
            curveTo(13.66f, 21f, 15f, 19.66f, 15f, 18f)
            curveTo(15f, 16.65f, 14.1f, 15.51f, 12.86f, 15.15f)
            lineTo(15.15f, 12.86f)
            curveTo(15.51f, 14.1f, 16.65f, 15f, 18f, 15f)
            curveTo(19.66f, 15f, 21f, 13.66f, 21f, 12f)
            curveTo(21f, 10.34f, 19.66f, 9f, 18f, 9f)
            curveTo(16.65f, 9f, 15.51f, 9.9f, 15.15f, 11.14f)
            lineTo(12.86f, 8.85f)
            curveTo(14.1f, 8.49f, 15f, 7.35f, 15f, 6f)
            curveTo(15f, 4.34f, 13.66f, 3f, 12f, 3f)
            close()
        }
    }.build()
}

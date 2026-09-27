package com.obsidian.shipathon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import web.navigator.navigator

class JsPlatform: Platform {
    private val userAgent = navigator.userAgent
    private val browserList = listOf("Chrome", "Firefox", "Safari", "Edge")

    override val name: String = userAgent.findAnyOf(browserList, ignoreCase = true)
            ?.let { (startIndex) -> userAgent.substring(startIndex).substringBefore(" ") }
            ?: "Unknown"
}

actual fun getPlatform(): Platform = JsPlatform()

@Composable
actual fun rememberShareLauncher(): (text: String, title: String) -> Unit {
    return remember {
        { _, _ -> }
    }
}

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op for JS browser
}
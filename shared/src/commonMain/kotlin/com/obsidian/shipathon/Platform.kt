package com.obsidian.shipathon

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun rememberShareLauncher(): (text: String, title: String) -> Unit

@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)
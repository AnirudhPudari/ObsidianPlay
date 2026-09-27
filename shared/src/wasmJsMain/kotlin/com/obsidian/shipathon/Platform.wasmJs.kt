package com.obsidian.shipathon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

@Composable
actual fun rememberShareLauncher(): (text: String, title: String) -> Unit {
    return remember {
        { _, _ -> }
    }
}

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op for Wasm
}
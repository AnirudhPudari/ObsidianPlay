package com.obsidian.shipathon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun rememberShareLauncher(): (text: String, title: String) -> Unit {
    return remember {
        { text, _ ->
            val activityController = UIActivityViewController(
                activityItems = listOf(text),
                applicationActivities = null
            )
            val window = UIApplication.sharedApplication.keyWindow
            val rootViewController = window?.rootViewController
            rootViewController?.presentViewController(activityController, animated = true, completion = null)
        }
    }
}

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No hardware back button on iOS
}
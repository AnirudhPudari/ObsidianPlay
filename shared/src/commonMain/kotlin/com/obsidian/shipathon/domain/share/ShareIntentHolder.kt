package com.obsidian.shipathon.domain.share

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Global singleton state holder for incoming shared text from social media / system share sheets.
 */
object ShareIntentHolder {
    private val _pendingShareText = MutableStateFlow<String?>(null)
    val pendingShareText: StateFlow<String?> = _pendingShareText.asStateFlow()

    fun onShareReceived(sharedText: String?) {
        if (!sharedText.isNullOrBlank()) {
            _pendingShareText.value = sharedText
        }
    }

    fun consumeShare() {
        _pendingShareText.value = null
    }
}

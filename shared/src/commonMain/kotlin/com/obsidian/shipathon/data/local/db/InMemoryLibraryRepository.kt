package com.obsidian.shipathon.data.local.db

import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryRepository
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.domain.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryLibraryRepository : LibraryRepository {
    private val _entries = MutableStateFlow<List<LibraryEntry>>(emptyList())
    override val entries: StateFlow<List<LibraryEntry>> = _entries.asStateFlow()

    override suspend fun save(game: Game, status: LibraryStatus) {
        val now = currentTimeMillis()
        val existing = _entries.value.firstOrNull { it.gameId == game.id }
        val entry = existing?.copy(
            name = game.name,
            summary = game.summary,
            coverUrl = game.coverUrl,
            genres = game.genres,
            platforms = game.platforms,
            themes = game.themes,
            rating = game.rating,
            firstReleaseDate = game.firstReleaseDate,
            status = status,
            progressPercent = defaultProgress(status, existing.progressPercent),
            updatedAt = now,
            lastPlayedAt = if (status == LibraryStatus.PLAYING) now else existing.lastPlayedAt,
        ) ?: LibraryEntry(
            gameId = game.id,
            name = game.name,
            summary = game.summary,
            coverUrl = game.coverUrl,
            genres = game.genres,
            platforms = game.platforms,
            themes = game.themes,
            rating = game.rating,
            firstReleaseDate = game.firstReleaseDate,
            status = status,
            progressPercent = defaultProgress(status),
            playtimeHours = 0.0,
            addedAt = now,
            updatedAt = now,
            lastPlayedAt = if (status == LibraryStatus.PLAYING) now else null,
        )
        upsert(entry)
    }

    override suspend fun updateProgress(gameId: Long, progressPercent: Int, playtimeHours: Double) {
        val existing = _entries.value.firstOrNull { it.gameId == gameId } ?: return
        val now = currentTimeMillis()
        upsert(
            existing.copy(
                progressPercent = progressPercent.coerceIn(0, 100),
                playtimeHours = playtimeHours.coerceAtLeast(0.0),
                status = if (progressPercent >= 100) LibraryStatus.COMPLETED else existing.status,
                updatedAt = now,
                lastPlayedAt = now,
            )
        )
    }

    override suspend fun updateRating(gameId: Long, rating: Double) {
        val existing = _entries.value.firstOrNull { it.gameId == gameId } ?: return
        val now = currentTimeMillis()
        upsert(
            existing.copy(
                rating = rating,
                updatedAt = now,
            )
        )
    }

    override suspend fun remove(gameId: Long) {
        _entries.value = _entries.value.filterNot { it.gameId == gameId }
    }

    private fun upsert(entry: LibraryEntry) {
        _entries.value = _entries.value
            .filterNot { it.gameId == entry.gameId }
            .plus(entry)
            .sortedByDescending { it.updatedAt }
    }
}

private fun defaultProgress(status: LibraryStatus, fallback: Int = 0): Int = when (status) {
    LibraryStatus.BACKLOG -> 0
    LibraryStatus.PLAYING -> fallback.coerceAtLeast(10)
    LibraryStatus.COMPLETED -> 100
}

private fun currentTimeMillis(): Long = kotlin.time.Clock.System.now().toEpochMilliseconds()


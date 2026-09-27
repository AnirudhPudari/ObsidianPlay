package com.obsidian.shipathon.domain.library

import com.obsidian.shipathon.domain.model.Game
import kotlinx.coroutines.flow.StateFlow

interface LibraryRepository {
    val entries: StateFlow<List<LibraryEntry>>

    suspend fun save(game: Game, status: LibraryStatus)
    suspend fun updateProgress(gameId: Long, progressPercent: Int, playtimeHours: Double)
    suspend fun updateRating(gameId: Long, rating: Double)
    suspend fun remove(gameId: Long)
}

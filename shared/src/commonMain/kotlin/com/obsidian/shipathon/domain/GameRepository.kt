package com.obsidian.shipathon.domain

import com.obsidian.shipathon.domain.model.Game

/**
 * Data-source-agnostic contract for game data. Keeping this interface in commonMain and screens
 * depending only on it (never on IgdbGameRepository directly) is what lets the backing API be
 * swapped without touching UI code - the RAWG API going dark mid-project is exactly the failure
 * mode this guards against.
 */
interface GameRepository {
    suspend fun searchGames(query: String, limit: Int = 20): Result<List<Game>>
    suspend fun getPopularGames(limit: Int = 20): Result<List<Game>>
    suspend fun getNewReleases(limit: Int = 20): Result<List<Game>>
    suspend fun getGameDetails(id: Long): Result<Game>
}

package com.obsidian.shipathon.data.remote.igdb

import com.obsidian.shipathon.domain.GameRepository
import com.obsidian.shipathon.domain.model.Game
import com.obsidian.shipathon.domain.model.SimilarGame
import io.ktor.client.HttpClient

class IgdbGameRepository(
    httpClient: HttpClient = createIgdbHttpClient(),
) : GameRepository {
    private val authProvider = IgdbAuthProvider(httpClient)
    private val api = IgdbApi(httpClient, authProvider)

    // In-memory cache to handle screen switches, temporary offline, and latency spikes
    private var cachedPopular: List<Game> = emptyList()
    private var cachedNewReleases: List<Game> = emptyList()

    override suspend fun searchGames(query: String, limit: Int): Result<List<Game>> = runCatching {
        api.search(query, limit).map { it.toDomain() }
    }

    override suspend fun getPopularGames(limit: Int): Result<List<Game>> = runCatching {
        try {
            val games = api.popular(limit).map { it.toDomain() }
            if (games.isNotEmpty()) {
                cachedPopular = games
            }
            games
        } catch (e: Throwable) {
            if (cachedPopular.isNotEmpty()) {
                println("Returning ${cachedPopular.size} cached popular games due to network issue: ${e.message}")
                cachedPopular
            } else {
                throw cleanException(e)
            }
        }
    }

    override suspend fun getNewReleases(limit: Int): Result<List<Game>> = runCatching {
        try {
            val games = api.newReleases(limit).map { it.toDomain() }
            if (games.isNotEmpty()) {
                cachedNewReleases = games
            }
            games
        } catch (e: Throwable) {
            if (cachedNewReleases.isNotEmpty()) {
                println("Returning ${cachedNewReleases.size} cached new releases due to network issue: ${e.message}")
                cachedNewReleases
            } else {
                throw cleanException(e)
            }
        }
    }

    override suspend fun getGameDetails(id: Long): Result<Game> = runCatching {
        try {
            api.byId(id)?.toDomain() ?: error("Game $id not found on IGDB")
        } catch (e: Throwable) {
            // Check in cached lists first
            val inCache = cachedPopular.firstOrNull { it.id == id }
                ?: cachedNewReleases.firstOrNull { it.id == id }
            inCache ?: throw cleanException(e)
        }
    }

    private fun cleanException(e: Throwable): Exception {
        val msg = e.message.orEmpty()
        return when {
            msg.contains("Socket timeout", ignoreCase = true) ||
            msg.contains("timed out", ignoreCase = true) ||
            msg.contains("timeout", ignoreCase = true) ->
                Exception("Network connection timed out. Please tap to retry.")
            msg.contains("Unable to resolve host", ignoreCase = true) ||
            msg.contains("ConnectException", ignoreCase = true) ->
                Exception("No internet connection. Please check your network.")
            else ->
                Exception(e.message ?: "Unable to fetch games from IGDB.")
        }
    }
}

private fun GameDto.toDomain(): Game = Game(
    id = id,
    name = name,
    summary = summary,
    coverUrl = igdbImageUrl(cover?.image_id, IgdbImageSize.CoverBig),
    genres = genres.map { it.name },
    platforms = platforms.map { it.name },
    themes = themes.map { it.name },
    rating = rating,
    firstReleaseDate = first_release_date,
    similarGames = similar_games.map {
        SimilarGame(
            id = it.id,
            name = it.name,
            coverUrl = igdbImageUrl(it.cover?.image_id, IgdbImageSize.CoverSmall),
        )
    },
)

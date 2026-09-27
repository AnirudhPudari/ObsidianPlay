package com.obsidian.shipathon.data.remote.igdb

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import kotlin.time.Clock

private const val IGDB_BASE_URL = "https://api.igdb.com/v4"

private const val GAME_FIELDS =
    "fields id,name,summary,rating,first_release_date," +
        "cover.image_id,genres.name,platforms.name,themes.name," +
        "similar_games.name,similar_games.cover.image_id;"

/** Resilient retry wrapper with exponential backoff for transient network drops / socket timeouts */
private suspend fun <T> withNetworkRetry(
    maxRetries: Int = 3,
    initialDelayMs: Long = 800,
    block: suspend () -> T,
): T {
    var currentDelay = initialDelayMs
    var lastException: Throwable? = null
    for (attempt in 1..maxRetries) {
        try {
            return block()
        } catch (e: Exception) {
            lastException = e
            println("IGDB Network attempt $attempt/$maxRetries failed: ${e.message}. Retrying in ${currentDelay}ms...")
            if (attempt < maxRetries) {
                delay(currentDelay)
                currentDelay = (currentDelay * 1.5).toLong()
            }
        }
    }
    throw lastException ?: Exception("Network request failed after $maxRetries attempts")
}

/** Thin wrapper over IGDB's Apicalypse query API - see https://api-docs.igdb.com/ */
internal class IgdbApi(
    private val httpClient: HttpClient,
    private val authProvider: IgdbAuthProvider,
) {
    private suspend fun games(query: String): List<GameDto> = withNetworkRetry {
        val token = authProvider.getAccessToken()
        httpClient.post("$IGDB_BASE_URL/games") {
            header("Client-ID", authProvider.clientId)
            header("Authorization", "Bearer $token")
            contentType(ContentType.Text.Plain)
            setBody(query)
        }.body()
    }

    suspend fun search(query: String, limit: Int): List<GameDto> {
        val safeQuery = query.replace("\"", "").replace(";", "").trim()
        return games("""$GAME_FIELDS search "$safeQuery"; limit $limit;""")
    }

    suspend fun popular(limit: Int): List<GameDto> =
        games("$GAME_FIELDS where rating != null & rating_count > 40; sort rating desc; limit $limit;")

    suspend fun newReleases(limit: Int): List<GameDto> {
        val nowSeconds = Clock.System.now().epochSeconds
        return games(
            "$GAME_FIELDS where first_release_date != null & first_release_date < $nowSeconds; " +
                "sort first_release_date desc; limit $limit;"
        )
    }

    suspend fun byId(id: Long): GameDto? =
        games("$GAME_FIELDS where id = $id;").firstOrNull()
}

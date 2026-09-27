package com.obsidian.shipathon.data.remote.igdb

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

/**
 * Fetches and caches a Twitch client-credentials token for IGDB, refreshing it a minute before
 * expiry. Guarded by a mutex so concurrent requests don't each trigger their own token fetch.
 */
internal class IgdbAuthProvider(
    private val httpClient: HttpClient,
    val clientId: String = IgdbSecrets.CLIENT_ID,
    private val clientSecret: String = IgdbSecrets.CLIENT_SECRET,
) {
    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var expiresAt: Instant = Instant.DISTANT_PAST

    suspend fun getAccessToken(): String = mutex.withLock {
        val now = Clock.System.now()
        cachedToken?.let { token -> if (now < expiresAt) return@withLock token }

        val response: TwitchTokenResponse = httpClient.post("https://id.twitch.tv/oauth2/token") {
            parameter("client_id", clientId)
            parameter("client_secret", clientSecret)
            parameter("grant_type", "client_credentials")
        }.body()

        cachedToken = response.access_token
        expiresAt = now + (response.expires_in.seconds - 60.seconds)
        response.access_token
    }
}

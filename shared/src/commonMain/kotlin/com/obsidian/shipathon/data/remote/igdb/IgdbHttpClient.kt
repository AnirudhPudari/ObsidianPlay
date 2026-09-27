package com.obsidian.shipathon.data.remote.igdb

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun createIgdbHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(HttpTimeout) {
        connectTimeoutMillis = 20_000
        socketTimeoutMillis = 35_000
        requestTimeoutMillis = 45_000
    }
    install(Logging) {
        level = LogLevel.INFO

        logger = object : Logger {
            override fun log(message: String) {
                println("HTTP Client: $message")
            }
        }
    }
}

package com.obsidian.shipathon.data.local.db

import com.obsidian.shipathon.domain.library.LibraryEntry
import com.obsidian.shipathon.domain.library.LibraryRepository
import com.obsidian.shipathon.domain.library.LibraryStatus
import com.obsidian.shipathon.domain.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SqlDelightLibraryRepository(
    private val database: ObsidianDatabase,
) : LibraryRepository {
    private val queries = database.libraryQueries
    private val _entries = MutableStateFlow(loadAll())
    override val entries: StateFlow<List<LibraryEntry>> = _entries.asStateFlow()

    override suspend fun save(game: Game, status: LibraryStatus) {
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val existing = queries.selectById(game.id).executeAsOneOrNull()?.toDomain()
        val next = (existing ?: LibraryEntry(
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
            lastPlayedAt = null,
        )).copy(
            name = game.name,
            summary = game.summary,
            coverUrl = game.coverUrl,
            genres = game.genres,
            platforms = game.platforms,
            themes = game.themes,
            rating = game.rating,
            firstReleaseDate = game.firstReleaseDate,
            status = status,
            progressPercent = defaultProgress(status, existing?.progressPercent ?: 0),
            updatedAt = now,
            lastPlayedAt = if (status == LibraryStatus.PLAYING) now else existing?.lastPlayedAt,
            playtimeHours = existing?.playtimeHours ?: 0.0,
        )
        queries.upsert(
            game.id,
            next.name,
            next.summary,
            next.coverUrl,
            encodeList(next.genres),
            encodeList(next.platforms),
            encodeList(next.themes),
            next.rating,
            next.firstReleaseDate,
            next.status.name,
            next.progressPercent.toLong(),
            next.playtimeHours,
            next.addedAt,
            next.updatedAt,
            next.lastPlayedAt,
        )
        refresh()
    }

    override suspend fun updateProgress(gameId: Long, progressPercent: Int, playtimeHours: Double) {
        val existing = queries.selectById(gameId).executeAsOneOrNull()?.toDomain() ?: return
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val next = existing.copy(
            progressPercent = progressPercent.coerceIn(0, 100),
            playtimeHours = playtimeHours.coerceAtLeast(0.0),
            status = if (progressPercent >= 100) LibraryStatus.COMPLETED else existing.status,
            updatedAt = now,
            lastPlayedAt = now,
        )
        queries.upsert(
            next.gameId,
            next.name,
            next.summary,
            next.coverUrl,
            encodeList(next.genres),
            encodeList(next.platforms),
            encodeList(next.themes),
            next.rating,
            next.firstReleaseDate,
            next.status.name,
            next.progressPercent.toLong(),
            next.playtimeHours,
            next.addedAt,
            next.updatedAt,
            next.lastPlayedAt,
        )
        refresh()
    }

    override suspend fun updateRating(gameId: Long, rating: Double) {
        val existing = queries.selectById(gameId).executeAsOneOrNull()?.toDomain() ?: return
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val next = existing.copy(
            rating = rating,
            updatedAt = now,
        )
        queries.upsert(
            next.gameId,
            next.name,
            next.summary,
            next.coverUrl,
            encodeList(next.genres),
            encodeList(next.platforms),
            encodeList(next.themes),
            next.rating,
            next.firstReleaseDate,
            next.status.name,
            next.progressPercent.toLong(),
            next.playtimeHours,
            next.addedAt,
            next.updatedAt,
            next.lastPlayedAt,
        )
        refresh()
    }

    override suspend fun remove(gameId: Long) {
        queries.deleteById(gameId)
        refresh()
    }

    private fun refresh() {
        _entries.value = loadAll()
    }

    private fun loadAll(): List<LibraryEntry> = queries.selectAll().executeAsList().map { it.toDomain() }

    private fun SelectAll.toDomain(): LibraryEntry = libraryEntryOf(
        gameId, name, summary, coverUrl, genresCsv, platformsCsv, themesCsv, rating,
        firstReleaseDate, status, progressPercent, playtimeHours, addedAt, updatedAt, lastPlayedAt,
    )

    private fun SelectById.toDomain(): LibraryEntry = libraryEntryOf(
        gameId, name, summary, coverUrl, genresCsv, platformsCsv, themesCsv, rating,
        firstReleaseDate, status, progressPercent, playtimeHours, addedAt, updatedAt, lastPlayedAt,
    )
}

private fun libraryEntryOf(
    gameId: Long,
    name: String,
    summary: String?,
    coverUrl: String?,
    genresCsv: String,
    platformsCsv: String,
    themesCsv: String,
    rating: Double?,
    firstReleaseDate: Long?,
    status: String,
    progressPercent: Long,
    playtimeHours: Double,
    addedAt: Long,
    updatedAt: Long,
    lastPlayedAt: Long?,
): LibraryEntry = LibraryEntry(
    gameId = gameId,
    name = name,
    summary = summary,
    coverUrl = coverUrl,
    genres = decodeList(genresCsv),
    platforms = decodeList(platformsCsv),
    themes = decodeList(themesCsv),
    rating = rating,
    firstReleaseDate = firstReleaseDate,
    status = LibraryStatus.valueOf(status),
    progressPercent = progressPercent.toInt(),
    playtimeHours = playtimeHours,
    addedAt = addedAt,
    updatedAt = updatedAt,
    lastPlayedAt = lastPlayedAt,
)

private fun defaultProgress(status: LibraryStatus, fallback: Int = 0): Int = when (status) {
    LibraryStatus.BACKLOG -> 0
    LibraryStatus.PLAYING -> fallback.coerceAtLeast(10)
    LibraryStatus.COMPLETED -> 100
}

private fun encodeList(values: List<String>): String = values.joinToString(LIST_SEPARATOR)
private fun decodeList(value: String): List<String> =
    if (value.isBlank()) emptyList() else value.split(LIST_SEPARATOR).filter { it.isNotBlank() }

private const val LIST_SEPARATOR = "\u001F"


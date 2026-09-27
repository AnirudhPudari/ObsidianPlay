package com.obsidian.shipathon.domain.library

import com.obsidian.shipathon.domain.model.Game

data class LibraryEntry(
    val gameId: Long,
    val name: String,
    val summary: String?,
    val coverUrl: String?,
    val genres: List<String>,
    val platforms: List<String>,
    val themes: List<String>,
    val rating: Double?,
    val firstReleaseDate: Long?,
    val status: LibraryStatus,
    val progressPercent: Int,
    val playtimeHours: Double,
    val addedAt: Long,
    val updatedAt: Long,
    val lastPlayedAt: Long?,
) {
    fun toGame(): Game = Game(
        id = gameId,
        name = name,
        summary = summary,
        coverUrl = coverUrl,
        genres = genres,
        platforms = platforms,
        themes = themes,
        rating = rating,
        firstReleaseDate = firstReleaseDate,
    )
}


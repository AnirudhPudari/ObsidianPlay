package com.obsidian.shipathon.domain.model

data class Game(
    val id: Long,
    val name: String,
    val summary: String?,
    val coverUrl: String?,
    val genres: List<String>,
    val platforms: List<String>,
    val themes: List<String>,
    val rating: Double?,
    val firstReleaseDate: Long?,
    val similarGames: List<SimilarGame> = emptyList(),
)

data class SimilarGame(
    val id: Long,
    val name: String,
    val coverUrl: String?,
)

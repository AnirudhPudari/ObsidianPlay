package com.obsidian.shipathon.data.remote.igdb

import kotlinx.serialization.Serializable

@Serializable
internal data class GameDto(
    val id: Long,
    val name: String,
    val summary: String? = null,
    val rating: Double? = null,
    val first_release_date: Long? = null,
    val cover: CoverDto? = null,
    val genres: List<NamedRefDto> = emptyList(),
    val platforms: List<NamedRefDto> = emptyList(),
    val themes: List<NamedRefDto> = emptyList(),
    val similar_games: List<SimilarGameDto> = emptyList(),
)

@Serializable
internal data class SimilarGameDto(
    val id: Long,
    val name: String,
    val cover: CoverDto? = null,
)

@Serializable
internal data class CoverDto(
    val image_id: String? = null,
)

@Serializable
internal data class NamedRefDto(
    val id: Long,
    val name: String,
)

@Serializable
internal data class TwitchTokenResponse(
    val access_token: String,
    val expires_in: Long,
    val token_type: String,
)

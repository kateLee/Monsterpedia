package com.katelee.monsterpedia.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PokemonDto(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "url") val url: String? = null,
)

@JsonClass(generateAdapter = true)
data class PokemonListResponse(
    @field:Json(name = "results") val results: List<PokemonDto> = emptyList(),
    @field:Json(name = "count") val count: Int? = null,
    @field:Json(name = "next") val next: String? = null,
    @field:Json(name = "previous") val previous: String? = null,
)

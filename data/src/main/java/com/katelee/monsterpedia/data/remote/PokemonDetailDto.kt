package com.katelee.monsterpedia.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "types") val types: List<PokemonTypeDto>,
    @field:Json(name = "stats") val stats: List<PokemonStatDto>,
    @field:Json(name = "height") val height: Int,
    @field:Json(name = "weight") val weight: Int,
)

@JsonClass(generateAdapter = true)
data class PokemonTypeDto(
    @field:Json(name = "slot") val slot: Int,
    @field:Json(name = "type") val type: PokemonTypeDetailDto,
)

@JsonClass(generateAdapter = true)
data class PokemonTypeDetailDto(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "url") val url: String,
)

@JsonClass(generateAdapter = true)
data class PokemonStatDto(
    @field:Json(name = "stat") val stat: PokemonStatDetailDto,
    @field:Json(name = "base_stat") val baseStat: Int,
    @field:Json(name = "effort") val effort: Int,
)

@JsonClass(generateAdapter = true)
data class PokemonStatDetailDto(
    @field:Json(name = "name") val name: String,
)

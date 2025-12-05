package com.katelee.monsterpedia.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class DigimonDto(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "href") val href: String? = null,
    @field:Json(name = "image") val image: String = "",
)

@JsonClass(generateAdapter = true)
data class DigimonListResponse(
    @field:Json(name = "content") val content: List<DigimonDto> = emptyList(),
    @field:Json(name = "pageable") val pageable: Pageable,
)

@JsonClass(generateAdapter = true)
data class Pageable(
    @field:Json(name = "previousPage") val previousPage: String,
    @field:Json(name = "nextPage") val nextPage: String,
)
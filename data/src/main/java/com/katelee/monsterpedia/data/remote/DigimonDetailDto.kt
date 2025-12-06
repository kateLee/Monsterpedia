package com.katelee.monsterpedia.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DigimonDetailDto(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "images") val images: List<DigimonImageDto>,
    @field:Json(name = "types") val types: List<DigimonTypeDto>,
    )

@JsonClass(generateAdapter = true)
data class DigimonImageDto(
    @field:Json(name = "href") val href: String,
)

@JsonClass(generateAdapter = true)
data class DigimonTypeDto(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "type") val type: String,
)

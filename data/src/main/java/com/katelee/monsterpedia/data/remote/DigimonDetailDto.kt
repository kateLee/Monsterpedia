package com.katelee.monsterpedia.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DigimonDetailDto(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "images") val images: List<DigimonImageDto>,
    @field:Json(name = "types") val types: List<DigimonTypeDto>,
    @field:Json(name = "attributes") val attributes: List<DigimonAttributeDto>,
    @field:Json(name = "descriptions") val descriptions: List<DigimonDescriptionDto>,
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

@JsonClass(generateAdapter = true)
data class DigimonAttributeDto(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "attribute") val attribute: String,
)

@JsonClass(generateAdapter = true)
data class DigimonDescriptionDto(
    @field:Json(name = "origin") val origin: String,
    @field:Json(name = "language") val language: String,
    @field:Json(name = "description") val description: String,
)

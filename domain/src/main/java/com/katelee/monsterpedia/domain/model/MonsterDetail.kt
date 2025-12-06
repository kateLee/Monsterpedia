package com.katelee.monsterpedia.domain.model

data class MonsterDetail(
    val id: String,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val stats: List<Stat>,
    val height: Int?,
    val weight: Int?
)
data class Stat(
    val name: String,
    val value: Int,
    val max: Int
)

package com.katelee.monsterpedia.domain.model

data class MonsterDetail(
    val id: String,
    val name: String,
    val imageUrl: String,
    val types: List<String> = emptyList(),
    val stats: List<Stat> = emptyList(),
    val height: Int? = null,
    val weight: Int? = null,
    val description: String = "",
)
data class Stat(
    val name: String,
    val value: Int,
    val max: Int
)

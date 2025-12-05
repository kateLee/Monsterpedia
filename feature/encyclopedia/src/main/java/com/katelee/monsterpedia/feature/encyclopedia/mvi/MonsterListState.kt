package com.katelee.monsterpedia.feature.encyclopedia.mvi

import com.katelee.monsterpedia.domain.model.Monster

data class MonsterListState(
    val isLoading: Boolean = false,
    val items: List<Monster> = emptyList(),
    val error: String? = null,
    val query: String = ""
)
package com.katelee.monsterpedia.feature.encyclopedia.mvi

data class MonsterListState(
    val isLoading: Boolean = false,
    val error: String? = null,
)
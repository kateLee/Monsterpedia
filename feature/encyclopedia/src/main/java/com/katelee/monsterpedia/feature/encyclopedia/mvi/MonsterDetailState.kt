package com.katelee.monsterpedia.feature.encyclopedia.mvi

import com.katelee.monsterpedia.domain.model.MonsterDetail

data class MonsterDetailState(
    val isLoading: Boolean = false,
    val item: MonsterDetail? = null,
    val error: Throwable? = null
)
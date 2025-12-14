package com.katelee.monsterpedia.feature.encyclopedia.mvi

sealed class MonsterListEffect {
    data class NavigateToDetail(val id: String) : MonsterListEffect()
}
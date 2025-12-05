package com.katelee.monsterpedia.feature.encyclopedia.mvi

sealed interface MonsterListIntent {
    object Load : MonsterListIntent
    data class Select(val id: String) : MonsterListIntent
}
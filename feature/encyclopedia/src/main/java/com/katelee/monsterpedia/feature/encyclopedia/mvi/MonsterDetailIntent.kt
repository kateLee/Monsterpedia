package com.katelee.monsterpedia.feature.encyclopedia.mvi

sealed interface MonsterDetailIntent {
    data class Load(val id: String): MonsterDetailIntent
    data object Retry : MonsterDetailIntent
}
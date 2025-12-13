package com.katelee.monsterpedia.feature.encyclopedia.mvi

import androidx.compose.ui.graphics.Color

sealed interface MonsterDetailIntent {
    data class Load(val id: String): MonsterDetailIntent
    data object Retry : MonsterDetailIntent
    data class ColorExtracted(val color: Color) : MonsterDetailIntent
}
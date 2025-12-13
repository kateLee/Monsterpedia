package com.katelee.monsterpedia.feature.encyclopedia.mvi

import androidx.compose.ui.graphics.Color

sealed class MonsterDetailEffect {
    data class NotifyTopBarColorUpdate(val color: Color) : MonsterDetailEffect()
    data class NotifyTopBarIdUpdate(val id: String) : MonsterDetailEffect()
}

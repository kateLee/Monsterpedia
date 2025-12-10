package com.katelee.monsterpedia.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState

    fun setColor(color: Color) {
        _uiState.update { it.copy(themeColor = color) }
    }
}

data class AppUiState(
    val themeColor: Color = Color.White
)
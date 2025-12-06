package com.katelee.monsterpedia.feature.encyclopedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailIntent
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonsterDetailViewModel @Inject constructor(
    private val repo: MonsterRepository
) : ViewModel() {
    private var currentId: String? = null

    private val _state = MutableStateFlow(MonsterDetailState())
    val state: StateFlow<MonsterDetailState> = _state.asStateFlow()

    fun onIntent(intent: MonsterDetailIntent) {
        when(intent) {
            is MonsterDetailIntent.Load -> {
                currentId = intent.id
                load(intent.id)
            }
            MonsterDetailIntent.Retry -> currentId?.let { load(it) }
        }
    }

    private fun load(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching { repo.getDetail(id) }
                .onSuccess { item ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            item = item
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception
                        )
                    }
                }
        }
    }

}
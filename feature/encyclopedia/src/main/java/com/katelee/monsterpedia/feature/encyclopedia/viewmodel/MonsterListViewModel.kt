package com.katelee.monsterpedia.feature.encyclopedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterListIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonsterListViewModel @Inject constructor(
    private val repo: MonsterRepository
) : ViewModel() {

    val pagingDataFlow = repo.getPaged().cachedIn(viewModelScope)

    private val intentChannel = Channel<MonsterListIntent>(Channel.Factory.UNLIMITED)

    init {
        viewModelScope.launch {
            for (intent in intentChannel) handle(intent)
        }
        intentChannel.trySend(MonsterListIntent.Load)
    }

    fun submit(intent: MonsterListIntent) {
        intentChannel.trySend(intent)
    }

    private suspend fun handle(intent: MonsterListIntent) {
        when (intent) {
            is MonsterListIntent.Load -> {
//                _state.update { it.copy(isLoading = true, error = null) }
//                runCatching { repo.getPaged().cachedIn(viewModelScope) }
//                    .onSuccess { items ->
//                        _state.update { it.copy(isLoading = false) }
//                    }
//                    .onFailure { exception ->
//                        _state.update { it.copy(isLoading = false, error = exception.message) }
//                    }
            }
            is MonsterListIntent.Select -> {
                // 發事件、導航或 set selected state
            }
        }
    }
}
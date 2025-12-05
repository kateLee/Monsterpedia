package com.katelee.monsterpedia.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.katelee.monsterpedia.data.remote.PokemonApi
import com.katelee.monsterpedia.data.remote.PokemonPagingSource
import com.katelee.monsterpedia.domain.model.Monster
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val api: PokemonApi) : MonsterRepository {
    override fun getPaged(): Flow<PagingData<Monster>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokemonPagingSource(api) }
        ).flow
}
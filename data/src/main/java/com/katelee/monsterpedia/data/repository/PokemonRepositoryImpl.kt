package com.katelee.monsterpedia.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.katelee.monsterpedia.data.remote.PokemonApi
import com.katelee.monsterpedia.data.remote.PokemonPagingSource
import com.katelee.monsterpedia.domain.model.Monster
import com.katelee.monsterpedia.domain.model.MonsterDetail
import com.katelee.monsterpedia.domain.model.Stat
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val api: PokemonApi) : MonsterRepository {
    override fun getPaged(): Flow<PagingData<Monster>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false,
                initialLoadSize = 10, // 為了避免頁數亂掉，不用原始3倍
            ),
            pagingSourceFactory = { PokemonPagingSource(api) }
        ).flow

    override suspend fun getDetail(id: String): MonsterDetail = api.getMonsterDetail(id).run {
        MonsterDetail(id = id,
            name = name,
            imageUrl = id.run { "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$this.png" },
            types = types.map { it.type.name },
            stats = stats.map {
                Stat(name = it.stat.name, value = it.effort, max = it.baseStat)
            },
            height = height,
            weight = weight,
        )
    }
}
package com.katelee.monsterpedia.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.katelee.monsterpedia.data.remote.DigimonApi
import com.katelee.monsterpedia.data.remote.DigimonPagingSource
import com.katelee.monsterpedia.domain.model.Monster
import com.katelee.monsterpedia.domain.model.MonsterDetail
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DigimonRepositoryImpl @Inject constructor(private val api: DigimonApi) : MonsterRepository {
    override fun getPaged(): Flow<PagingData<Monster>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { DigimonPagingSource(api) }
        ).flow

    override suspend fun getDetail(id: String): MonsterDetail = api.getMonsterDetail(id).run {
        MonsterDetail(
            id = id,
            name = name,
            imageUrl = images.firstOrNull()?.href ?: "",
            types = types.map { it.type },
            attributes = attributes.map { it.attribute },
            description = descriptions.firstOrNull { it.language == "en_us" }?.description ?: "",
        )
    }
}
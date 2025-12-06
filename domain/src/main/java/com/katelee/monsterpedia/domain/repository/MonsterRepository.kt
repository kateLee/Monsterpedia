package com.katelee.monsterpedia.domain.repository

import androidx.paging.PagingData
import com.katelee.monsterpedia.domain.model.Monster
import com.katelee.monsterpedia.domain.model.MonsterDetail
import kotlinx.coroutines.flow.Flow

interface MonsterRepository {
    fun getPaged(): Flow<PagingData<Monster>>
    suspend fun getDetail(id: String): MonsterDetail
}
package com.katelee.monsterpedia.domain.repository

import com.katelee.monsterpedia.domain.model.Monster

interface MonsterRepository {
    suspend fun getAll(): List<Monster>
}
package com.katelee.monsterpedia.data.repository

import com.katelee.monsterpedia.domain.model.Monster
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor() : MonsterRepository {
    override suspend fun getAll() = listOf(
        Monster("1", "bulbasaur","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"),
        Monster("2", "ivysaur","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png"),
    )
}
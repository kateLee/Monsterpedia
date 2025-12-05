package com.katelee.monsterpedia.data.repository

import com.katelee.monsterpedia.data.remote.PokemonApi
import com.katelee.monsterpedia.domain.model.Monster
import com.katelee.monsterpedia.domain.repository.MonsterRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val api: PokemonApi) : MonsterRepository {
    override suspend fun getAll(): List<Monster> {
        val response = api.getMonsters()
        return response.results.map {
            val id = it.url?.substringAfter("pokemon/")?.substringBefore("/")
            Monster(
                id = id ?: "",
                name = it.name,
                imageUrl = id?.run { "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$this.png" } ?: "")
        }
    }
//        listOf(
//        Monster("1", "bulbasaur","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"),
//        Monster("2", "ivysaur","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png"),
//    )
}
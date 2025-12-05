package com.katelee.monsterpedia.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {
    @GET("pokemon")
    suspend fun getMonsters(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): PokemonListResponse

//    @GET("pokemon/{id}")
//    suspend fun getMonsterDetail(
//        @Path("id") id: String
//    ): PokemonDto
}
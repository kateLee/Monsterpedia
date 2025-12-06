package com.katelee.monsterpedia.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DigimonApi {
    @GET("digimon")
    suspend fun getMonsters(
        @Query("pageSize") pageSize: Int = 10,
        @Query("page") page: Int = 0,
    ): DigimonListResponse

    @GET("digimon/{id}")
    suspend fun getMonsterDetail(
        @Path("id") id: String
    ): DigimonDetailDto
}
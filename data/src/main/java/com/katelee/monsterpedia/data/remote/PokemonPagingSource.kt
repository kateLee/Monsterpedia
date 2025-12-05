package com.katelee.monsterpedia.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.katelee.monsterpedia.domain.model.Monster

class PokemonPagingSource(private val api: PokemonApi) : PagingSource<Int, Monster>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Monster> {
        val page = params.key ?: 1

        return try {
            val response = api.getMonsters(offset = (page - 1) * params.loadSize, limit = params.loadSize)
            LoadResult.Page(
                data = response.results.map {
                    val id = it.url?.substringAfter("pokemon/")?.substringBefore("/")
                    Monster(
                        id = id ?: "",
                        name = it.name,
                        imageUrl = id?.run { "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$this.png" } ?: "")
                },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Monster>): Int? =
        state.anchorPosition
}
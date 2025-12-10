package com.katelee.monsterpedia.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.katelee.monsterpedia.domain.model.Monster

class DigimonPagingSource(private val api: DigimonApi) : PagingSource<Int, Monster>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Monster> {
        val page = params.key ?: 0

        return try {
            val response = api.getMonsters(page = page, pageSize = params.loadSize)
            LoadResult.Page(
                data = response.content.map {
                    Monster(
                        id = it.id.toString(),
                        name = it.name,
                        imageUrl = it.image
                    )
                },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.pageable.nextPage.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Monster>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}
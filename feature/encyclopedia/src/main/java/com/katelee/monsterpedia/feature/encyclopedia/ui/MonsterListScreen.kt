package com.katelee.monsterpedia.feature.encyclopedia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.katelee.monsterpedia.feature.encyclopedia.R
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterListIntent
import com.katelee.monsterpedia.feature.encyclopedia.viewmodel.MonsterListViewModel
import com.katelee.monsterpedia.ui.ErrorScreen
import com.katelee.monsterpedia.ui.ListPlaceholder
import com.katelee.monsterpedia.ui.LoadingScreen

@Composable
fun MonsterListScreen(
    viewModel: MonsterListViewModel,
) {
    val lazyPagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems()

    when (lazyPagingItems.loadState.refresh) {
        is LoadState.Loading -> LoadingScreen()
        is LoadState.Error -> ErrorScreen(
            error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
        )
        is LoadState.NotLoading -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey { it.id }) { index ->
                    lazyPagingItems[index]?.run {
                        MonsterRow(monster = this, onClick = { viewModel.submit(MonsterListIntent.Select(id)) })
                    }
                }

                // append 狀態 (載入更多) - 佔滿一整行
                item(span = { GridItemSpan(maxLineSpan) }) {
                    when (lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            ListPlaceholder()
                        }
                        is LoadState.Error -> {
                            Button(
                                onClick = { lazyPagingItems.retry() }
                            ) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
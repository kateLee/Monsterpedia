package com.katelee.monsterpedia.feature.encyclopedia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.katelee.monsterpedia.feature.encyclopedia.viewmodel.MonsterListViewModel

@Composable
fun MonsterListScreen(
    viewModel: MonsterListViewModel = viewModel(),
    onSelect: (String) -> Unit
) {
    val lazyPagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems()

    when (lazyPagingItems.loadState.refresh) {
        is LoadState.Loading -> LoadingScreen()
        is LoadState.Error -> ErrorScreen(
            error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
        )
        is LoadState.NotLoading -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey { it.id }) { index ->
                    lazyPagingItems[index]?.run {
                        MonsterRow(monster = this, onClick = { onSelect(id) })
                    }
                }

                // append 狀態 (載入更多) - 佔滿一整行
                item(span = { GridItemSpan(maxLineSpan) }) {
                    when (lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            MonsterPlaceholder()
                        }
                        is LoadState.Error -> {
                            Button(
                                onClick = { lazyPagingItems.retry() }
                            ) {
                                Text("Retry")
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun MonsterPlaceholder() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(error: Throwable) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(error.message ?: "Unknown error")
    }
}
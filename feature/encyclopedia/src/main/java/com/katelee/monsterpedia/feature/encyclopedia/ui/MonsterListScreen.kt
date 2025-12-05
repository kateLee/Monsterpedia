package com.katelee.monsterpedia.feature.encyclopedia.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.katelee.monsterpedia.feature.encyclopedia.viewmodel.MonsterListViewModel

@Composable
fun MonsterListScreen(
    viewModel: MonsterListViewModel = viewModel(),
    onSelect: (String) -> Unit
) {
    val lazyPagingItems = viewModel.monsters.collectAsLazyPagingItems()

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey { it.id }) { index ->
            lazyPagingItems[index]?.run {
                MonsterRow(monster = this, onClick = { onSelect(id) })
            } ?: run {
                MonsterPlaceholder()
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
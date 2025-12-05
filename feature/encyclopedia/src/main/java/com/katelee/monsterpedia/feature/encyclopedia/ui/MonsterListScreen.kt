package com.katelee.monsterpedia.feature.encyclopedia.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.katelee.monsterpedia.feature.encyclopedia.viewmodel.MonsterListViewModel

@Composable
fun MonsterListScreen(
    viewModel: MonsterListViewModel = viewModel(),
    onSelect: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (state.items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No monsters found")
        }
    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(state.items, key = { monster -> monster.id }) { monster ->
                MonsterRow(monster = monster, onClick = { onSelect(monster.id) })
            }
        }
    }
}
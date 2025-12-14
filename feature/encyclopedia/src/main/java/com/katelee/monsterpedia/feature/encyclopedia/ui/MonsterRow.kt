package com.katelee.monsterpedia.feature.encyclopedia.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.katelee.monsterpedia.domain.model.Monster
import com.katelee.monsterpedia.ui.ImageWithDominantColor

@Composable
fun MonsterRow(
    monster: Monster,
    onClick: () -> Unit
) {
    var dominantColor by remember { mutableStateOf(Color.Gray) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = dominantColor
        )
    ) {
        ImageWithDominantColor(monster.imageUrl,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.padding(8.dp).fillMaxWidth()) {
            dominantColor = it
        }
        Text(monster.name,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            maxLines = 1,
            textAlign = TextAlign.Center,
            )
    }
}
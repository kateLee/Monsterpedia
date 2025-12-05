package com.katelee.monsterpedia.feature.encyclopedia.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.katelee.monsterpedia.domain.model.Monster

@Composable
fun MonsterRow(
    monster: Monster,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
            .data(monster.imageUrl)
            .crossfade(true)
            .build(),
////            error = painterResource(R.drawable.ic_broken_image),
////            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = "monster image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(monster.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
            maxLines = 1,
            textAlign = TextAlign.Center,
            )
    }
}
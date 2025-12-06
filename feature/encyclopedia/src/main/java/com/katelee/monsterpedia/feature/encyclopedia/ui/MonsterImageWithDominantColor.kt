package com.katelee.monsterpedia.feature.encyclopedia.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.toBitmap

@Composable
fun MonsterImageWithDominantColor(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale,
    onDominantColor: (Color) -> Unit
) {
    val context = LocalContext.current

    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false) // Palette 必需禁用 hardware
        .crossfade(true)
        .build()

    AsyncImage(
        model = request,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        onSuccess = { success ->
            val bitmap = success.result.image.toBitmap()

            Palette.from(bitmap).generate { palette ->
                val color = palette?.getDominantColor(0x808080)
                if (color != null) {
                    onDominantColor(Color(color))
                }
            }
        }
    )
}
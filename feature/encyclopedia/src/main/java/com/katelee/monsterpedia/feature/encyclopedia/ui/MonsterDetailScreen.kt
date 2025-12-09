package com.katelee.monsterpedia.feature.encyclopedia.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.katelee.monsterpedia.domain.model.Stat
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailIntent
import com.katelee.monsterpedia.feature.encyclopedia.viewmodel.MonsterDetailViewModel

@Composable
fun MonsterDetailScreen(
    viewModel: MonsterDetailViewModel,
    id: String,
) {
    LaunchedEffect(id) {
        viewModel.onIntent(MonsterDetailIntent.Load(id))
    }

    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        LoadingScreen()
    } else {
        state.error?.let {
            ErrorScreen(error = it)
        }
        state.item?.let { monster ->
            Column(modifier = Modifier.background(Color.Black).fillMaxHeight()) {
                var dominantColor by remember { mutableStateOf(Color.Gray) }
                Card(modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = dominantColor
                    )) {
                    MonsterImageWithDominantColor(monster.imageUrl,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.height(300.dp).fillMaxWidth()) {
                        dominantColor = it
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    monster.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MonsterTypeChips(modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(), types = monster.types)
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    TitleInfo("Weight", "${monster.weight ?: "???"} KG")
                    TitleInfo("Height", "${monster.height ?: "???"} M")
                }
                if (!monster.stats.isEmpty()) {
                    MonsterBaseStats(
                        modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
                        stats = monster.stats
                    )
                }
                Text(monster.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),)
            }
        }
    }
}

@Composable
fun MonsterBaseStats(modifier: Modifier, stats: List<Stat>) {
    Column(modifier = modifier) {
        Text(
            "Base Stats",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            textAlign = TextAlign.Center,
        )
        stats.forEach { stat ->
            StatType.fromString(stat.name)?.let { statType ->
                StatBar(
                    statType = statType,
                    value = stat.max,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
@Composable
fun StatBar(
    statType: StatType,
    value: Int,
    modifier: Modifier = Modifier,
    showAnimation: Boolean = true) {
    val maxValue = statType.max
    val progress = (value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f)

    // 動畫效果
    val animatedProgress by animateFloatAsState(
        targetValue = if (showAnimation) progress else progress,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )
    Row(modifier = modifier) {
        Text(statType.displayName,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.widthIn(min = 60.dp)
            )
        // 進度條容器
        Box(
            modifier = Modifier
                .weight(1f)
                .height(24.dp)
        ) {
            // 背景
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0))
            )

            // 進度
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(12.dp))
                    .background(statType.color)
            )

            // 數值文字 (在進度條內)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (progress > 0.2f)
                    Text(
                        text = "$value/$maxValue",
                        style = MaterialTheme.typography.bodySmall,
                        modifier =  Modifier.align(Alignment.CenterStart)
                                .fillMaxWidth(progress)
                                .padding(end = 8.dp)
                                .wrapContentWidth(Alignment.End),
                        color = Color.White
                    )
                else {
                    Row {
                        Spacer(modifier = Modifier.fillMaxWidth(progress).padding(end = 8.dp))
                        Text(
                            text = "$value/$maxValue",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonsterTypeChips(
    types: List<String>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround) {
        types.forEach { type ->
            TypeChip(type)
        }
    }
}

@Composable
private fun TypeChip(type: String) {
    AssistChip(
        onClick = {},
        label = {
            Text(type,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 160.dp),)
        },
        leadingIcon = null,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = chipColor(type),
            labelColor = Color.White
        ),
    )
}

private fun chipColor(type: String): Color {
    return when (type.lowercase()) {
        "fire" -> Color(0xFFF08030)
        "water" -> Color(0xFF6890F0)
        "grass" -> Color(0xFF78C850)
        "electric" -> Color(0xFFF8D030)
        "ice" -> Color(0xFF98D8D8)
        "dragon" -> Color(0xFF7038F8)
        "dark" -> Color(0xFF705848)
        "fairy" -> Color(0xFFEE99AC)
        else -> Color.Gray
    }
}

// 屬性類型
enum class StatType(val displayName: String, val key: String, val color: Color, val max: Int = 300) {
    HP("HP", "hp", Color(0xFFFF5959)),
    ATK("ATK", "attack", Color(0xFFF08030)),
    DEF("DEF", "defense", Color(0xFF6890F0)),
    SPEED("SPD", "speed", Color(0xFFF85888)),
    EXP("EXP", "experience", Color(0xFF78C850), max = 1000);

    companion object {
        // 方法一: 從 key 轉換 (推薦,支援多種格式)
        fun fromString(value: String): StatType? {
            return when (value.lowercase().replace("-", "").replace("_", "").replace(".", "")) {
                "hp" -> HP
                "attack", "atk" -> ATK
                "defense", "def" -> DEF
                "speed", "spd" -> SPEED
                "experience", "exp" -> EXP
                else -> null
            }
        }
    }
}

@Composable
private fun TitleInfo(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            maxLines = 1,
        )
        Text(title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            maxLines = 1,
            )
    }
}
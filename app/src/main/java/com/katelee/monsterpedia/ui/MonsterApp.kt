package com.katelee.monsterpedia.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.katelee.monsterpedia.R
import com.katelee.monsterpedia.navigation.MonsterNavGraph
import com.katelee.monsterpedia.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonsterApp(navController: NavHostController = rememberNavController()) {
    var topBarColor by remember { mutableStateOf(Color.White) }
    var monsterId by remember { mutableStateOf<String?>(null) }

    val currentRoute = navController.currentBackStackEntryFlow
        .collectAsState(initial = null).value?.destination?.route

    Scaffold(
        topBar = {
            when(currentRoute) {
                Routes.LIST ->
                    TopAppBar(title = { Text(stringResource(R.string.app_name),
                        color = Color.White) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = colorResource(R.color.theme_color)
                        ))
                Routes.DETAIL -> {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name),
                            color = Color.White) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = topBarColor),
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack,
                                    "Back",
                                    tint = Color.White)
                            }
                        },
                        actions = { Text("#${monsterId ?: ""}",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp)) }
                    )
                }
            }
        }
    ) { padding ->
        MonsterNavGraph(
            navController = navController,
            modifier = Modifier.padding(padding),
            // 傳遞狀態更新的 Lambda 函式
            onUpdateScaffoldColor = { newColor -> topBarColor = newColor },
            onUpdateScaffoldId = { newId -> monsterId = newId }
        )
    }
}
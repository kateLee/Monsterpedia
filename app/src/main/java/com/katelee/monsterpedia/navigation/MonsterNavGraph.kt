package com.katelee.monsterpedia.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.katelee.monsterpedia.feature.encyclopedia.mvi.MonsterDetailEffect
import com.katelee.monsterpedia.feature.encyclopedia.ui.MonsterDetailScreen
import com.katelee.monsterpedia.feature.encyclopedia.ui.MonsterListScreen
import com.katelee.monsterpedia.feature.encyclopedia.viewmodel.MonsterDetailViewModel

@Composable
fun MonsterNavGraph(navController: NavHostController, modifier: Modifier,
                    onUpdateScaffoldColor: (Color) -> Unit,
                    onUpdateScaffoldId: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LIST,
        modifier = modifier,
    ) {
        composable(Routes.LIST) { backStackEntry ->
            MonsterListScreen(
                viewModel = hiltViewModel(backStackEntry),
                onSelect = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }

        composable(Routes.DETAIL) { backStackEntry ->
            val viewModel = hiltViewModel<MonsterDetailViewModel>(backStackEntry)

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is MonsterDetailEffect.NotifyTopBarColorUpdate -> {
                            onUpdateScaffoldColor(effect.color)
                        }
                        is MonsterDetailEffect.NotifyTopBarIdUpdate -> {
                            onUpdateScaffoldId(effect.id)
                        }
                    }
                }
            }
            MonsterDetailScreen(
                id = backStackEntry.arguments?.getString("id") ?: "",
                viewModel = viewModel,
            )
        }
    }
}
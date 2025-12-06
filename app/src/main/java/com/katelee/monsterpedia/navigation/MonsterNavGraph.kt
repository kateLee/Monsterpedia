package com.katelee.monsterpedia.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.katelee.monsterpedia.feature.encyclopedia.ui.MonsterDetailScreen
import com.katelee.monsterpedia.feature.encyclopedia.ui.MonsterListScreen

@Composable
fun MonsterNavGraph(navController: NavHostController, modifier: Modifier) {
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
            MonsterDetailScreen(
                id = backStackEntry.arguments?.getString("id") ?: "",
                viewModel = hiltViewModel(backStackEntry),
                )
        }
    }
}
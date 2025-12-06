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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.katelee.monsterpedia.R
import com.katelee.monsterpedia.navigation.MonsterNavGraph
import com.katelee.monsterpedia.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonsterApp() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryFlow
        .collectAsState(initial = null).value?.destination?.route

    Scaffold(
        topBar = {
            when(currentRoute) {
                Routes.LIST ->
                    TopAppBar(title = { Text(stringResource(R.string.app_name)) })
                Routes.DETAIL -> {
                    val id = navController.currentBackStackEntryFlow
                        .collectAsState(initial = navController.currentBackStackEntry).value?.arguments?.getString("id")
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        },
                        actions = { Text("#${id ?: ""}") }
                    )
                }
            }
        }
    ) { padding ->
        MonsterNavGraph(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
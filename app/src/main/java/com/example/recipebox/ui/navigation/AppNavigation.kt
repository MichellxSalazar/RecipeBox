package com.example.recipebox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipebox.ui.screens.AddRecipeScreen
import com.example.recipebox.ui.screens.HomeScreen
import com.example.recipebox.ui.viewmodels.MainViewModel

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("add_recipe") {
            AddRecipeScreen(navController = navController, viewModel = viewModel)
        }
    }
}

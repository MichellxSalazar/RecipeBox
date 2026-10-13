package com.example.recipebox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipebox.ui.screens.AddRecipeScreen
import com.example.recipebox.ui.screens.EditRecipeScreen
import com.example.recipebox.ui.screens.HomeScreen
import com.example.recipebox.ui.screens.RecipeDetailScreen
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
        composable(
            route = "recipe_detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            RecipeDetailScreen(recipeId = recipeId, navController = navController, viewModel = viewModel)
        }
        composable(
            route = "edit_recipe/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            EditRecipeScreen(recipeId = recipeId, navController = navController, viewModel = viewModel)
        }
    }
}

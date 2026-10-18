package com.example.recipebox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipebox.ui.inicio.screens.HomeScreen
import com.example.recipebox.ui.inicio.viewmodels.HomeViewModel
import com.example.recipebox.ui.recetas.screens.AddRecipeScreen
import com.example.recipebox.ui.recetas.screens.EditRecipeScreen
import com.example.recipebox.ui.recetas.screens.RecipeDetailScreen
import com.example.recipebox.ui.recetas.viewmodels.RecetasViewModel
import com.example.recipebox.ui.ingredientes.screens.OrganizerScreen
import com.example.recipebox.ui.ingredientes.viewmodels.OrganizerViewModel
import com.example.recipebox.ui.configuracion.screens.ConfiguracionScreen
import com.example.recipebox.ui.configuracion.viewmodels.ConfiguracionViewModel
import com.example.recipebox.ui.modococina.screens.CookingModeScreen
import com.example.recipebox.ui.modococina.viewmodels.RecipeDetailViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                onAddRecipeClick = { navController.navigate("add_recipe") },
                onRecipeClick = { recipe -> navController.navigate("recipe_detail/${recipe.id}") },
                onOrganizerClick = { navController.navigate("organizer") },
                onSettingsClick = { navController.navigate("configuracion") }
            )
        }
        composable("add_recipe") {
            val viewModel = hiltViewModel<RecetasViewModel>()
            AddRecipeScreen(
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable(
            route = "recipe_detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: -1L
            val viewModel = hiltViewModel<RecetasViewModel>()
            RecipeDetailScreen(
                recipeId = recipeId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id -> navController.navigate("edit_recipe/$id") },
                onStartCooking = { id -> navController.navigate("cooking_mode/$id") },
                viewModel = viewModel
            )
        }
        composable(
            route = "edit_recipe/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: -1L
            val viewModel = hiltViewModel<RecetasViewModel>()
            EditRecipeScreen(
                recipeId = recipeId,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable("organizer") {
            val viewModel = hiltViewModel<OrganizerViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            OrganizerScreen(
                uiState = uiState,
                onTabSelected = viewModel::onTabSelected,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onAddClick = { 
                    if (uiState.selectedTab.name == "CATEGORIES") viewModel.openNewCategoryDialog() 
                    else viewModel.openNewIngredientDialog() 
                },
                onEditCategory = viewModel::openEditCategoryDialog,
                onDeleteCategory = viewModel::deleteCategory,
                onCategoryNameChange = viewModel::onCategoryNameChange,
                onCategoryDescriptionChange = viewModel::onCategoryDescriptionChange,
                onSaveCategory = viewModel::saveCategory,
                onDismissCategoryDialog = viewModel::dismissCategoryDialog,
                onEditIngredient = viewModel::openEditIngredientDialog,
                onDeleteIngredient = viewModel::deleteIngredient,
                onIngredientNameChange = viewModel::onIngredientNameChange,
                onIngredientUnitChange = viewModel::onIngredientUnitChange,
                onSaveIngredient = viewModel::saveIngredient,
                onDismissIngredientDialog = viewModel::dismissIngredientDialog,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("configuracion") {
            val viewModel = hiltViewModel<ConfiguracionViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ConfiguracionScreen(
                uiState = uiState,
                onPantallaEncendidaChange = viewModel::cambiarPantallaEncendida,
                onSistemaMetricoChange = viewModel::cambiarSistemaMetrico,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = "cooking_mode/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: -1L
            val viewModel = hiltViewModel<RecipeDetailViewModel>()
            CookingModeScreen(
                recipeId = recipeId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
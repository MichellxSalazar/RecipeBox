package com.example.recipebox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipebox.ui.configuracion.screens.ConfiguracionRoute
import com.example.recipebox.ui.screens.AddRecipeScreen
import com.example.recipebox.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destino.Inicio.ruta
    ) {
        composable(Destino.Inicio.ruta) {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = Destino.RecetaForm.ruta,
            arguments = listOf(
                navArgument(ArgsNav.RECETA_ID) {
                    type = NavType.LongType
                    defaultValue = ArgsNav.SIN_ID
                }
            )
        ) {
            AddRecipeScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(Destino.Configuracion.ruta) {
            ConfiguracionRoute(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
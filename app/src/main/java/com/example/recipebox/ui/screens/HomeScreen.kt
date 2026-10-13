package com.example.recipebox.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipebox.ui.components.RecipeCard
import com.example.recipebox.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val recipes by viewModel.allRecipes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("RecipeBox") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_recipe") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Recipe")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (recipes.isEmpty()) {
                Text(
                    text = "No recipes found. Start adding some!",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(recipes) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { navController.navigate("recipe_detail/${recipe.id}") }
                        )
                    }
                }
            }
        }
    }
}

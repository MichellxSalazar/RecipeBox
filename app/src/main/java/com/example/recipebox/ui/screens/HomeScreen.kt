
package com.example.recipebox.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.ui.home.components.RecipeCard
import com.example.recipebox.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val recipes by viewModel.allRecipes.collectAsState()

    HomeContent(
        recipes = recipes,
        onAddRecipe = {
            navController.navigate("add_recipe")
        },
        onRecipeClick = {
            // La navegación al detalle se conectará después
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    recipes: List<RecipeEntity>,
    onAddRecipe: () -> Unit,
    onRecipeClick: (RecipeEntity) -> Unit
) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    val filteredRecipes = recipes.filter {
        it.name.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RecipeBox",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecipe,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar receta"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Mis recetas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Encuentra algo delicioso para preparar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar recetas...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true,
                shape = androidx.compose.foundation.shape
                    .RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Todas las recetas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "${filteredRecipes.size} recetas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredRecipes.isEmpty()) {
                Text(
                    text = if (searchText.isBlank()) {
                        "No tienes recetas guardadas. ¡Agrega una!"
                    } else {
                        "No se encontraron recetas."
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 20.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(
                        bottom = 88.dp
                    )
                ) {
                    items(
                        items = filteredRecipes,
                        key = { it.id }
                    ) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = {
                                onRecipeClick(recipe)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RecipeListPreview() {

    val recetasEjemplo = listOf(
        RecipeEntity(
            id = 1,
            name = "Arroz con pollo",
            categoryId = 1,
            ingredients = "Arroz y pollo",
            instructions = "Preparar el arroz",
            prepTimeMinutes = 40,
            isFavorite = true
        ),
        RecipeEntity(
            id = 2,
            name = "Ensalada de atún",
            categoryId = 1,
            ingredients = "Atún y vegetales",
            instructions = "Mezclar ingredientes",
            prepTimeMinutes = 15,
            isFavorite = false
        ),
        RecipeEntity(
            id = 3,
            name = "Pasta con queso",
            categoryId = 1,
            ingredients = "Pasta y queso",
            instructions = "Cocinar la pasta",
            prepTimeMinutes = 25,
            isFavorite = true
        )
    )

    MaterialTheme {
        HomeContent(
            recipes = recetasEjemplo,
            onAddRecipe = {},
            onRecipeClick = {}
        )
    }
}

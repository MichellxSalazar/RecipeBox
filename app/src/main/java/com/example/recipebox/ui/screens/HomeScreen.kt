
package com.example.recipebox.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.ui.home.components.RecipeCard
import com.example.recipebox.ui.viewmodels.MainViewModel
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.recipebox.ui.home.HomeUiState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipebox.data.local.RecipeDatabase
import com.example.recipebox.repository.RecipeRepository
import com.example.recipebox.ui.home.HomeViewModel
import com.example.recipebox.ui.home.HomeViewModelFactory
import com.example.recipebox.ui.home.RecipeFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current.applicationContext

    val repository = remember(context) {
        val database = RecipeDatabase.getDatabase(context)
        RecipeRepository(database.recipeDao())
    }

    val homeViewModel: HomeViewModel = viewModel(
        factory = remember(repository) {
            HomeViewModelFactory(repository)
        }
    )

    val uiState by homeViewModel.uiState.collectAsState()

    HomeStateContent(
        uiState = uiState,
        onAddRecipe = {
            navController.navigate("add_recipe")
        },
        onRecipeClick = {
            // Navegación al detalle pendiente de integración
        },
        onRetry = {
            homeViewModel.loadRecipes()
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


    var onlyFavorites by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedCategoryId by rememberSaveable {
        mutableIntStateOf(0)
    }

    var selectedOrder by rememberSaveable {
        mutableStateOf("default")
    }

    var showOrderMenu by remember {
        mutableStateOf(false)
    }



    val filteredRecipes = RecipeFilter.filterAndSort(
        recipes = recipes,
        searchText = searchText,
        onlyFavorites = onlyFavorites,
        selectedCategoryId = selectedCategoryId,
        selectedOrder = selectedOrder
    )

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
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchText = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            )


            Spacer(modifier = Modifier.height(20.dp))


            val categoryIds = recipes
                .map { it.categoryId }
                .distinct()
                .sorted()

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        androidx.compose.foundation.rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategoryId == 0,
                    onClick = { selectedCategoryId = 0 },
                    label = { Text("Todas las recetas") }
                )

                categoryIds.forEach { categoryId ->
                    FilterChip(
                        selected = selectedCategoryId == categoryId,
                        onClick = {
                            selectedCategoryId = categoryId
                        },
                        label = {
                            Text("Categoría $categoryId")
                        }
                    )
                }
            }



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = !onlyFavorites,
                    onClick = { onlyFavorites = false },
                    label = { Text("Todas") }
                )

                FilterChip(
                    selected = onlyFavorites,
                    onClick = { onlyFavorites = true },
                    label = { Text("♥ Favoritas") }
                )

                Box {
                    OutlinedButton(
                        onClick = { showOrderMenu = true },
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                    ) {
                        Text("Ordenar")
                    }

                    DropdownMenu(
                        expanded = showOrderMenu,
                        onDismissRequest = { showOrderMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Orden original") },
                            onClick = {
                                selectedOrder = "default"
                                showOrderMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Nombre (A-Z)") },
                            onClick = {
                                selectedOrder = "az"
                                showOrderMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Nombre (Z-A)") },
                            onClick = {
                                selectedOrder = "za"
                                showOrderMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Menor tiempo") },
                            onClick = {
                                selectedOrder = "fast"
                                showOrderMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Mayor tiempo") },
                            onClick = {
                                selectedOrder = "slow"
                                showOrderMenu = false
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            val hasActiveFilters =
                searchText.isNotBlank() ||
                        onlyFavorites ||
                        selectedCategoryId != 0 ||
                        selectedOrder != "default"

            if (hasActiveFilters) {
                TextButton(
                    onClick = {
                        searchText = ""
                        onlyFavorites = false
                        selectedCategoryId = 0
                        selectedOrder = "default"
                    }
                ) {
                    Text("Limpiar filtros")
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (onlyFavorites) {
                        "Mis favoritas"
                    } else {
                        "Todas las recetas"
                    },
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

                if (recipes.isEmpty()) {

                    EmptySearchState(
                        title = "Todavía no tienes recetas",
                        message = "Comienza agregando tu primera receta.",
                        buttonText = "Agregar receta",
                        onAction = onAddRecipe
                    )

                } else {

                    EmptySearchState(
                        title = "No encontramos recetas",
                        message = "Intenta buscar con otro nombre o limpia los filtros.",
                        buttonText = "Limpiar búsqueda",
                        onAction = {
                            searchText = ""
                            onlyFavorites = false
                        }
                    )
                }

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
            categoryId = 2,
            ingredients = "Atún y vegetales",
            instructions = "Mezclar ingredientes",
            prepTimeMinutes = 15,
            isFavorite = false
        ),
        RecipeEntity(
            id = 3,
            name = "Pasta con queso",
            categoryId = 3,
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

@Composable
fun EmptySearchState(
    title: String,
    message: String,
    buttonText: String,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(52.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        OutlinedButton(
            onClick = onAction
        ) {
            Text(buttonText)
        }
    }
}


@Composable
fun LoadingRecipesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Cargando tus recetas...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun LoadingRecipesStatePreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingRecipesState()
            }
        }
    }
}


@Composable
private fun HomeStateContent(
    uiState: HomeUiState,
    onAddRecipe: () -> Unit,
    onRecipeClick: (RecipeEntity) -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            LoadingRecipesState()
        }

        is HomeUiState.Success -> {
            HomeContent(
                recipes = uiState.recipes,
                onAddRecipe = onAddRecipe,
                onRecipeClick = onRecipeClick
            )
        }


        is HomeUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "No pudimos cargar tus recetas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center

                    )
                    Button(
                        onClick = onRetry
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeLoadingPreview() {
    MaterialTheme {
        HomeStateContent(
            uiState = HomeUiState.Loading,
            onAddRecipe = {},
            onRecipeClick = {},
                    onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeEmptyPreview() {
    MaterialTheme {
        HomeStateContent(
            uiState = HomeUiState.Success(emptyList()),
            onAddRecipe = {},
            onRecipeClick = {},
                    onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeErrorPreview() {
    MaterialTheme {
        HomeStateContent(
            uiState = HomeUiState.Error(
                "No se pudieron cargar las recetas"
            ),
            onAddRecipe = {},
            onRecipeClick = {},
                    onRetry = {}
        )
    }
}



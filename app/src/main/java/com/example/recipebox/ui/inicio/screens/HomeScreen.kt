package com.example.recipebox.ui.inicio.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.ui.inicio.components.RecipeCard
import com.example.recipebox.ui.inicio.viewmodels.HomeUiState
import com.example.recipebox.ui.inicio.viewmodels.HomeViewModel
import com.example.recipebox.ui.inicio.viewmodels.RecipeFilter

// =====================================================
// PANTALLA PRINCIPAL - INTEGRACIÓN CON HILT & STATE HOISTING
// =====================================================

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAddRecipeClick: () -> Unit,
    onRecipeClick: (RecipeEntity) -> Unit = {},
    onOrganizerClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    // Observamos StateFlow respetando el ciclo de vida.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeStateContent(
        uiState = uiState,
        onAddRecipe = onAddRecipeClick,
        onRecipeClick = onRecipeClick,
        onOrganizerClick = onOrganizerClick,
        onSettingsClick = onSettingsClick,
        onRetry = {
            viewModel.loadRecipes()
        }
    )
}

// =====================================================
// CONTENIDO DE INICIO
// =====================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    recipes: List<RecipeEntity>,
    onAddRecipe: () -> Unit,
    onRecipeClick: (RecipeEntity) -> Unit,
    onOrganizerClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    var onlyFavorites by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedCategoryId by rememberSaveable {
        mutableLongStateOf(0L)
    }

    var selectedOrder by rememberSaveable {
        mutableStateOf("default")
    }

    var showOrderMenu by remember {
        mutableStateOf(false)
    }

    // Aplicamos la lógica de nuestro RecipeFilter.
    val filteredRecipes = RecipeFilter.filterAndSort(
        recipes = recipes,
        searchText = searchText,
        onlyFavorites = onlyFavorites,
        selectedCategoryId = selectedCategoryId,
        selectedOrder = selectedOrder
    )

    val categoryIds = recipes
        .map { it.categoryId }
        .distinct()
        .sorted()

    val hasActiveFilters =
        searchText.isNotBlank() ||
                onlyFavorites ||
                selectedCategoryId != 0L ||
                selectedOrder != "default"

    // Restablece todos los filtros.
    val clearFilters = {
        searchText = ""
        onlyFavorites = false
        selectedCategoryId = 0L
        selectedOrder = "default"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RecipeBox",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onOrganizerClick) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "Organizador"
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración"
                        )
                    }
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

            // =========================================
            // BUSCADOR DE RECETAS
            // =========================================

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Buscar por nombre")
                },
                placeholder = {
                    Text("Buscar recetas...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
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
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // =========================================
            // FILTRO POR CATEGORÍAS
            // =========================================

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FilterChip(
                    selected = selectedCategoryId == 0L,
                    onClick = {
                        selectedCategoryId = 0L
                    },
                    label = {
                        Text("Todas")
                    }
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

            // =========================================
            // FAVORITOS Y ORDENAMIENTO
            // =========================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                FilterChip(
                    selected = !onlyFavorites,
                    onClick = {
                        onlyFavorites = false
                    },
                    label = {
                        Text("Todas las recetas")
                    }
                )

                FilterChip(
                    selected = onlyFavorites,
                    onClick = {
                        onlyFavorites = true
                    },
                    label = {
                        Text("♥ Favoritas")
                    }
                )

                Box {

                    OutlinedButton(
                        onClick = {
                            showOrderMenu = true
                        },
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                    ) {
                        Text("Ordenar")
                    }

                    DropdownMenu(
                        expanded = showOrderMenu,
                        onDismissRequest = {
                            showOrderMenu = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text("Orden original")
                            },
                            onClick = {
                                selectedOrder = "default"
                                showOrderMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Nombre (A-Z)")
                            },
                            onClick = {
                                selectedOrder = "az"
                                showOrderMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Nombre (Z-A)")
                            },
                            onClick = {
                                selectedOrder = "za"
                                showOrderMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Menor tiempo")
                            },
                            onClick = {
                                selectedOrder = "fast"
                                showOrderMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Mayor tiempo")
                            },
                            onClick = {
                                selectedOrder = "slow"
                                showOrderMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // =========================================
            // LIMPIAR FILTROS
            // =========================================

            if (hasActiveFilters) {
                TextButton(
                    onClick = clearFilters
                ) {
                    Text("Limpiar filtros")
                }
            }

            // =========================================
            // CONTADOR DE RECETAS
            // =========================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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

            // =========================================
            // LISTA DE RECETAS O ESTADO VACÍO
            // =========================================

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
                        buttonText = "Limpiar filtros",
                        onAction = clearFilters
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

// =====================================================
// COMPONENTE PARA LISTAS VACÍAS
// =====================================================

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
            .padding(
                vertical = 40.dp,
                horizontal = 16.dp
            ),
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

// =====================================================
// ESTADO DE CARGA
// =====================================================

@Composable
fun LoadingRecipesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 48.dp,
                horizontal = 16.dp
            ),
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

// =====================================================
// STATEFUL / STATELESS - ESTADOS DE LA PANTALLA
// =====================================================

@Composable
private fun HomeStateContent(
    uiState: HomeUiState,
    onAddRecipe: () -> Unit,
    onRecipeClick: (RecipeEntity) -> Unit,
    onOrganizerClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onRetry: () -> Unit
) {
    when (uiState) {

        is HomeUiState.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingRecipesState()
            }
        }

        is HomeUiState.Success -> {

            HomeContent(
                recipes = uiState.recipes,
                onAddRecipe = onAddRecipe,
                onRecipeClick = onRecipeClick,
                onOrganizerClick = onOrganizerClick,
                onSettingsClick = onSettingsClick
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

// =====================================================
// DATOS FICTICIOS PARA PREVIEW
// =====================================================

private val previewRecipes = listOf(
    RecipeEntity(
        id = 1L,
        name = "Arroz con pollo",
        categoryId = 1L,
        ingredients = "Arroz y pollo",
        instructions = "Preparar el arroz",
        prepTimeMinutes = 40,
        isFavorite = true
    ),

    RecipeEntity(
        id = 2L,
        name = "Ensalada de atún",
        categoryId = 2L,
        ingredients = "Atún y vegetales",
        instructions = "Mezclar ingredientes",
        prepTimeMinutes = 15,
        isFavorite = false
    ),

    RecipeEntity(
        id = 3L,
        name = "Pasta con queso",
        categoryId = 3L,
        ingredients = "Pasta y queso",
        instructions = "Cocinar la pasta",
        prepTimeMinutes = 25,
        isFavorite = true
    )
)

// =====================================================
// PREVIEWS
// =====================================================

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RecipeListPreview() {
    MaterialTheme {
        HomeContent(
            recipes = previewRecipes,
            onAddRecipe = {},
            onRecipeClick = {}
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
        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingRecipesState()
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
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

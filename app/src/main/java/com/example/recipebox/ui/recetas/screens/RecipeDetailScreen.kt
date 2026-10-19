package com.example.recipebox.ui.recetas.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.ui.modococina.utils.UnitConverter
import com.example.recipebox.ui.recetas.viewmodels.RecetasViewModel
import com.example.recipebox.ui.recetas.viewmodels.RecipeEvent

// =====================================================
// CONTENEDOR CON ESTADO (STATEFUL COMPOSABLE)
// =====================================================

@Composable
fun RecipeDetailScreen(
    recipeId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onStartCooking: (Long) -> Unit,
    viewModel: RecetasViewModel = hiltViewModel()
) {
    val recipes by viewModel.allRecipes.collectAsStateWithLifecycle()
    val useMetricSystem by viewModel.useMetricSystem.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Consumo seguro de eventos puntuales (One-off events)
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RecipeEvent.RecipeDeleted -> onNavigateBack()
                is RecipeEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                else -> Unit
            }
        }
    }

    val recipe = recipes.find { it.id == recipeId }

    RecipeDetailContent(
        recipe = recipe,
        useMetricSystem = useMetricSystem,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack,
        onNavigateToEdit = onNavigateToEdit,
        onStartCooking = onStartCooking,
        onToggleFavorite = { r -> viewModel.updateRecipe(r.copy(isFavorite = !r.isFavorite)) },
        onDeleteRecipe = { r -> viewModel.deleteRecipe(r) }
    )
}

// =====================================================
// VISTA SIN ESTADO (STATELESS COMPOSABLE)
// Solo recibe estados inmutables y lambdas de acción
// =====================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailContent(
    recipe: RecipeEntity?,
    useMetricSystem: Boolean = true,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onStartCooking: (Long) -> Unit,
    onToggleFavorite: (RecipeEntity) -> Unit,
    onDeleteRecipe: (RecipeEntity) -> Unit
) {
    if (recipe == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Receta no encontrada.")
        }
        return
    }

    val ingredientsList = remember(recipe.ingredients, useMetricSystem) {
        recipe.ingredients
            .split("\n", ",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { UnitConverter.convertIngredientMeasurement(it, useMetricSystem) }
    }

    val stepsList = remember(recipe.instructions) {
        recipe.instructions
            .split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(recipe.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onToggleFavorite(recipe) }) {
                        Icon(
                            imageVector = if (recipe.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favorito",
                            tint = if (recipe.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { onNavigateToEdit(recipe.id) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar receta")
                    }
                    IconButton(onClick = { onDeleteRecipe(recipe) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar receta")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onStartCooking(recipe.id) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cocinar")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tiempo de preparación",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${recipe.prepTimeMinutes} minutos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SquareFoot,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (useMetricSystem) "Métrico (g, ml)" else "Imperial (oz, tazas)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ingredientsList.forEach { ingredient ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = ingredient,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Instrucciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                stepsList.forEachIndexed { index, step ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(88.dp))
        }
    }
}

package com.example.recipebox.ui.recetas.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tiempo de preparación",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${recipe.prepTimeMinutes} minutos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.ingredients,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Instrucciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.instructions,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

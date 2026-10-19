package com.example.recipebox.ui.recetas.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.ui.recetas.viewmodels.RecetasViewModel
import com.example.recipebox.ui.recetas.viewmodels.RecipeEvent

// =====================================================
// CONTENEDOR CON ESTADO (STATEFUL COMPOSABLE)
// =====================================================

@Composable
fun AddRecipeScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecetasViewModel = hiltViewModel()
) {
    val categories by viewModel.allCategories.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Consumo seguro de eventos puntuales (One-off events)
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RecipeEvent.RecipeSaved -> onNavigateBack()
                is RecipeEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                else -> Unit
            }
        }
    }

    AddRecipeContent(
        categories = categories,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack,
        onSaveRecipe = { recipe -> viewModel.addRecipe(recipe) }
    )
}

// =====================================================
// VISTA SIN ESTADO (STATELESS COMPOSABLE)
// Solo recibe estados inmutables y lambdas de acción
// =====================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeContent(
    categories: List<CategoryEntity>,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigateBack: () -> Unit,
    onSaveRecipe: (RecipeEntity) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var ingredients by rememberSaveable { mutableStateOf("") }
    var instructions by rememberSaveable { mutableStateOf("") }
    var prepTime by rememberSaveable { mutableStateOf("") }

    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf(1L) }
    val selectedCategoryName = categories.find { it.id == selectedCategory }?.name ?: "Seleccionar categoría"

    val isFormValid = name.isNotBlank() && ingredients.isNotBlank() && instructions.isNotBlank() && prepTime.isNotBlank()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Agregar receta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la receta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = prepTime,
                onValueChange = { prepTime = it },
                label = { Text("Tiempo de preparación (minutos)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Selector de categorías
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    value = selectedCategoryName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                )

                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategory = category.id
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("Ingredientes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text("Instrucciones") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        onSaveRecipe(
                            RecipeEntity(
                                name = name.trim(),
                                categoryId = selectedCategory,
                                ingredients = ingredients.trim(),
                                instructions = instructions.trim(),
                                prepTimeMinutes = prepTime.toIntOrNull() ?: 0
                            )
                        )
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar receta")
            }
        }
    }
}

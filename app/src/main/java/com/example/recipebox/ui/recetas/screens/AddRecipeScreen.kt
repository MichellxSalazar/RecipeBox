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
import com.example.recipebox.data.model.Ingredient
import com.example.recipebox.ui.recetas.components.IngredientInputSection
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
    val availableIngredients by viewModel.allIngredients.collectAsStateWithLifecycle()
    val useMetricSystem by viewModel.useMetricSystem.collectAsStateWithLifecycle()
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
        availableIngredients = availableIngredients,
        useMetricSystem = useMetricSystem,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack,
        onSaveRecipe = { recipe -> viewModel.addRecipe(recipe) },
        onSaveNewIngredient = viewModel::saveNewIngredient
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
    availableIngredients: List<Ingredient> = emptyList(),
    useMetricSystem: Boolean = true,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigateBack: () -> Unit,
    onSaveRecipe: (RecipeEntity) -> Unit,
    onSaveNewIngredient: (String, String, (Ingredient?) -> Unit) -> Unit = { _, _, cb -> cb(null) }
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = prepTime,
                onValueChange = { prepTime = it },
                label = { Text("Tiempo de preparación (minutos)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            // Selector interactivo de ingredientes con soporte de guardado automático
            IngredientInputSection(
                ingredientsText = ingredients,
                onIngredientsTextChange = { ingredients = it },
                availableIngredients = availableIngredients,
                onSaveNewIngredient = onSaveNewIngredient,
                useMetricSystem = useMetricSystem
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text("Instrucciones") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )

            Spacer(modifier = Modifier.height(20.dp))

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

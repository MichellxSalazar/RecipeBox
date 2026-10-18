package com.example.recipebox.ui.recetas.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recipebox.ui.recetas.viewmodels.RecetasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    recipeId: Long,
    onNavigateBack: () -> Unit,
    viewModel: RecetasViewModel
) {
    val recipes by viewModel.allRecipes.collectAsState()
    val categories by viewModel.allCategories.collectAsState()

    val recipeToEdit = recipes.find { it.id == recipeId }

    if (recipeToEdit == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Receta no encontrada")
        }
        return
    }

    var name by rememberSaveable { mutableStateOf(recipeToEdit.name) }
    var ingredients by rememberSaveable { mutableStateOf(recipeToEdit.ingredients) }
    var instructions by rememberSaveable { mutableStateOf(recipeToEdit.instructions) }
    var prepTime by rememberSaveable { mutableStateOf(recipeToEdit.prepTimeMinutes.toString()) }

    var expandedCategory by remember { mutableStateOf(false) }
    // selectedCategory usa Long para coincidir con CategoryEntity.id y RecipeEntity.categoryId
    var selectedCategory by rememberSaveable { mutableStateOf(recipeToEdit.categoryId) }
    val selectedCategoryName = categories.find { it.id == selectedCategory }?.name ?: "Seleccionar categoría"

    val isFormValid = name.isNotBlank() && ingredients.isNotBlank() && instructions.isNotBlank() && prepTime.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar receta") },
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Desplegable de categorías
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
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
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
                value = prepTime,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        prepTime = it
                    }
                },
                label = { Text("Tiempo de preparación (minutos)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

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
                        viewModel.updateRecipe(
                            recipeToEdit.copy(
                                name = name.trim(),
                                categoryId = selectedCategory,
                                ingredients = ingredients.trim(),
                                instructions = instructions.trim(),
                                prepTimeMinutes = prepTime.toIntOrNull() ?: 0
                            )
                        )
                        onNavigateBack()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar receta")
            }
        }
    }
}

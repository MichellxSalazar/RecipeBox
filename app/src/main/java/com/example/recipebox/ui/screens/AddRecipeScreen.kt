package com.example.recipebox.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(navController: NavController, viewModel: MainViewModel) {
    // Uso de rememberSaveable para sobrevivir a la rotación de pantalla
    var name by rememberSaveable { mutableStateOf("") }
    var ingredients by rememberSaveable { mutableStateOf("") }
    var instructions by rememberSaveable { mutableStateOf("") }
    
    // Validación Defensiva (no guardar si está vacío)
    val isFormValid = name.isNotBlank() && ingredients.isNotBlank() && instructions.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Recipe") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                // Evita que el teclado tape los campos
                .imePadding()
                // Scroll preventivo
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Recipe Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("Ingredients") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text("Instructions") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (isFormValid) {
                        viewModel.addRecipe(
                            RecipeEntity(
                                name = name.trim(),
                                categoryId = 1, // Default por ahora
                                ingredients = ingredients.trim(),
                                instructions = instructions.trim(),
                                prepTimeMinutes = 30 // Default por ahora
                            )
                        )
                        navController.popBackStack()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Recipe")
            }
        }
    }
}

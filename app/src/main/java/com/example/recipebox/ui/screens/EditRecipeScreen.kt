package com.example.recipebox.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipebox.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    recipeId: Int,
    navController: NavController,
    viewModel: MainViewModel
) {
    val recipes by viewModel.allRecipes.collectAsState()
    val recipeToEdit = recipes.find { it.id == recipeId }

    if (recipeToEdit == null) {
        Text("Recipe not found")
        return
    }

    var name by rememberSaveable { mutableStateOf(recipeToEdit.name) }
    var ingredients by rememberSaveable { mutableStateOf(recipeToEdit.ingredients) }
    var instructions by rememberSaveable { mutableStateOf(recipeToEdit.instructions) }

    val isFormValid = name.isNotBlank() && ingredients.isNotBlank() && instructions.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Recipe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                        viewModel.updateRecipe(
                            recipeToEdit.copy(
                                name = name.trim(),
                                ingredients = ingredients.trim(),
                                instructions = instructions.trim()
                            )
                        )
                        navController.popBackStack()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Recipe")
            }
        }
    }
}

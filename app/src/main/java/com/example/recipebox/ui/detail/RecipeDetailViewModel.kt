package com.example.recipebox.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Simulación de carga mientras se conecta con Room/Hilt
            _uiState.update {
                it.copy(
                    isLoading = false,
                    title = "Receta de Ejemplo",
                    description = "Descripción de la receta",
                    ingredients = listOf("Ingrediente 1", "Ingrediente 2"),
                    steps = listOf("Paso 1: Preparar ingredientes", "Paso 2: Cocinar", "Paso 3: Servir"),
                    errorMessage = null
                )
            }
        }
    }

    fun nextStep() {
        _uiState.update { currentState ->
            if (currentState.currentStepIndex < currentState.steps.size - 1) {
                currentState.copy(currentStepIndex = currentState.currentStepIndex + 1)
            } else currentState
        }
    }

    fun previousStep() {
        _uiState.update { currentState ->
            if (currentState.currentStepIndex > 0) {
                currentState.copy(currentStepIndex = currentState.currentStepIndex - 1)
            } else currentState
        }
    }

    fun resetCookingMode() {
        _uiState.update { it.copy(currentStepIndex = 0) }
    }
}
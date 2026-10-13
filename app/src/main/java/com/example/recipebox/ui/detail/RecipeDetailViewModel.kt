package com.example.recipebox.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getRecipeById(recipeId).collect { recipe ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recipe = recipe,
                            errorMessage = if (recipe == null) "Receta no encontrada" else null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Error al cargar la receta"
                    )
                }
            }
        }
    }

    fun nextStep() {
        _uiState.update { currentState ->
            val stepsCount = currentState.recipe?.pasos?.size ?: 0
            if (currentState.currentStepIndex < stepsCount - 1) {
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
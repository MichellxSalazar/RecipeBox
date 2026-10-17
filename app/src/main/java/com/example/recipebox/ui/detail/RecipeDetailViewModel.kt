package com.example.recipebox.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getRecipeById(recipeId).collect { recipeEntity ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recipe = recipeEntity,
                            title = recipeEntity?.titulo ?: "Sin título",
                            description = recipeEntity?.descripcion ?: "",
                            ingredients = recipeEntity?.ingredientes ?: emptyList(),
                            steps = recipeEntity?.pasos ?: emptyList(),
                            errorMessage = if (recipeEntity == null) "Receta no encontrada" else null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Error al cargar receta"
                    )
                }
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
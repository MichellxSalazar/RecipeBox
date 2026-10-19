package com.example.recipebox.ui.modococina.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.data.preferences.UserPreferencesRepository
import com.example.recipebox.repository.RecipeRepository
import com.example.recipebox.ui.modococina.utils.UnitConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Constructor secundario para pruebas unitarias sin Hilt
    constructor(repository: RecipeRepository) : this(repository, UserPreferencesRepository())

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val useMetric = preferencesRepository.useMetricSystemFlow.firstOrNull() ?: true
            val keepScreenOn = preferencesRepository.keepScreenOnFlow.firstOrNull() ?: false

            val recipe = repository.getRecipeById(recipeId)
            if (recipe != null) {
                val ingredientsList = recipe.ingredients
                    .split("\n", ",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { UnitConverter.convertIngredientMeasurement(it, useMetric) }

                val stepsList = recipe.instructions
                    .split("\n")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { UnitConverter.convertIngredientMeasurement(it, useMetric) }
                    .ifEmpty { listOf(recipe.instructions) }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        recipe = recipe,
                        title = recipe.name,
                        description = "Tiempo estimado: ${recipe.prepTimeMinutes} minutos",
                        ingredients = ingredientsList.ifEmpty { listOf("Ingredientes varios") },
                        steps = stepsList,
                        isKeepScreenOn = keepScreenOn,
                        errorMessage = null
                    )
                }
            } else {
                // Fallback a receta demostrativa por si el id no existe en base de datos
                val rawIngredients = listOf(
                    "400g de Pasta Spaghettini",
                    "150g de Guanciale o Tocineta",
                    "4 Yemas de huevo",
                    "100g de Queso Pecorino Romano",
                    "Pimienta negra recién molida"
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        title = "Pasta Carbonara Tradicional",
                        description = "Deliciosa receta italiana hecha a base de huevo, queso pecorino y guanciale.",
                        ingredients = rawIngredients.map { ing -> UnitConverter.convertIngredientMeasurement(ing, useMetric) },
                        steps = listOf(
                            "Paso 1: Poner a hervir abundante agua con sal en una olla grande.",
                            "Paso 2: Dorar el guanciale picado en cubos en una sartén a fuego medio.",
                            "Paso 3: Mezclar las yemas de huevo con el queso pecorino hasta formar una crema."
                        ),
                        isKeepScreenOn = keepScreenOn,
                        errorMessage = null
                    )
                }
            }
        }
    }

    // Sobrecarga para compatibilidad con Int
    fun loadRecipe(recipeId: Int) {
        loadRecipe(recipeId.toLong())
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

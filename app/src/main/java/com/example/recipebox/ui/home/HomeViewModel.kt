
package com.example.recipebox.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.repository.RecipeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        loadJob?.cancel()

        _uiState.value = HomeUiState.Loading

        loadJob = viewModelScope.launch {
            repository.getAllRecipes()
                .catch { exception ->
                    _uiState.value = HomeUiState.Error(
                        exception.message
                            ?: "No se pudieron cargar las recetas"
                    )
                }
                .collect { recipes ->
                    _uiState.value = HomeUiState.Success(recipes)
                }
        }
    }
}

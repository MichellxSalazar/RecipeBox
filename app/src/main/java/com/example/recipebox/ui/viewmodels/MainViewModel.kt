package com.example.recipebox.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.repository.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RecipeRepository) : ViewModel() {

    init {
        // Pre-populate database with default categories
        viewModelScope.launch {
            if (repository.getCategoryCount() == 0) {
                repository.insertCategories(
                    listOf(
                        CategoryEntity(id = 1, name = "Desayunos"),
                        CategoryEntity(id = 2, name = "Almuerzos"),
                        CategoryEntity(id = 3, name = "Cenas"),
                        CategoryEntity(id = 4, name = "Postres")
                    )
                )
            }
        }
    }

    // Estado reactivo explícito usando Sealed Class (RecipeUiState)
    val uiState: StateFlow<RecipeUiState> = repository.getAllRecipes()
        .map { recipes ->
            RecipeUiState.Success(recipes) as RecipeUiState
        }
        .catch {
            emit(RecipeUiState.Error("Error loading recipes"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecipeUiState.Loading
        )

    val allRecipes: StateFlow<List<RecipeEntity>> = repository.getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    val allCategories: StateFlow<List<CategoryEntity>> = flowOf(
        listOf(
            CategoryEntity(id = 1, name = "Desayunos"),
            CategoryEntity(id = 2, name = "Almuerzos"),
            CategoryEntity(id = 3, name = "Cenas"),
            CategoryEntity(id = 4, name = "Postres")
        )
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.insertRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.updateRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
        }
    }
}

class MainViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

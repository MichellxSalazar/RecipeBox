package com.example.recipebox.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.repository.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RecipeRepository) : ViewModel() {

    init {
        // Pre-populate database with default categories to avoid Foreign Key constraint failures
        // until Modulo-Categorias implements full category management.
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

    val allRecipes: StateFlow<List<RecipeEntity>> = repository.getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    // Simulated categories until Modulo-Categorias is merged. 
    // We use a StateFlow to match Room's behavior.
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

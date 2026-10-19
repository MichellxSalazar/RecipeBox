package com.example.recipebox.ui.inicio.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.data.local.DatabasePrepopulator
import com.example.recipebox.data.local.dao.CategoryDao
import com.example.recipebox.data.local.dao.IngredientDao
import com.example.recipebox.data.local.dao.RecipeDao
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.repository.CategoryRepository
import com.example.recipebox.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val recipeDao: RecipeDao,
    private val categoryDao: CategoryDao,
    private val ingredientDao: IngredientDao
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            DatabasePrepopulator.prepopulateIfNeeded(recipeDao, categoryDao, ingredientDao)
            loadRecipes()
        }
    }

    fun loadRecipes() {
        loadJob?.cancel()

        _uiState.value = HomeUiState.Loading

        loadJob = viewModelScope.launch {
            combine(
                repository.getAllRecipes(),
                categoryRepository.observeCategories()
            ) { recipes, categories ->
                val categoryEntities = categories.map {
                    CategoryEntity(id = it.id, name = it.name, description = it.description)
                }
                HomeUiState.Success(
                    recipes = recipes,
                    categories = categoryEntities
                ) as HomeUiState
            }
            .catch { exception ->
                _uiState.value = HomeUiState.Error(
                    exception.message ?: "No se pudieron cargar las recetas"
                )
            }
            .collect { state ->
                _uiState.value = state
            }
        }
    }
}

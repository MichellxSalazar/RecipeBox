package com.example.recipebox.ui.recetas.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.repository.CategoryRepository
import com.example.recipebox.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// Eventos puntuales (One-off events) para navegación y notificaciones
sealed interface RecipeEvent {
    object RecipeSaved : RecipeEvent
    object RecipeUpdated : RecipeEvent
    object RecipeDeleted : RecipeEvent
    data class ShowMessage(val message: String) : RecipeEvent
}

@HiltViewModel
class RecetasViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // Canal para eventos puntuales de una sola vez
    private val _events = Channel<RecipeEvent>(Channel.BUFFERED)
    val events: Flow<RecipeEvent> = _events.receiveAsFlow()

    init {
        // Inicializar categorías base si están vacías
        viewModelScope.launch {
            if (repository.getCategoryCount() == 0) {
                repository.insertCategories(
                    listOf(
                        CategoryEntity(id = 1L, name = "Desayunos"),
                        CategoryEntity(id = 2L, name = "Almuerzos"),
                        CategoryEntity(id = 3L, name = "Cenas"),
                        CategoryEntity(id = 4L, name = "Postres")
                    )
                )
            }
        }
    }

    val uiState: StateFlow<RecipeUiState> = repository.getAllRecipes()
        .map { recipes ->
            RecipeUiState.Success(recipes) as RecipeUiState
        }
        .catch {
            emit(RecipeUiState.Error("Error al cargar las recetas"))
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

    val allCategories: StateFlow<List<CategoryEntity>> = categoryRepository.observeCategories()
        .map { categories ->
            if (categories.isEmpty()) {
                listOf(
                    CategoryEntity(id = 1L, name = "Desayunos"),
                    CategoryEntity(id = 2L, name = "Almuerzos"),
                    CategoryEntity(id = 3L, name = "Cenas"),
                    CategoryEntity(id = 4L, name = "Postres")
                )
            } else {
                categories.map { CategoryEntity(id = it.id, name = it.name, description = it.description) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf(
                CategoryEntity(id = 1L, name = "Desayunos"),
                CategoryEntity(id = 2L, name = "Almuerzos"),
                CategoryEntity(id = 3L, name = "Cenas"),
                CategoryEntity(id = 4L, name = "Postres")
            )
        )

    fun addRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            try {
                repository.insertRecipe(recipe)
                _events.send(RecipeEvent.RecipeSaved)
            } catch (e: Exception) {
                _events.send(RecipeEvent.ShowMessage(e.message ?: "Error al guardar la receta"))
            }
        }
    }

    fun updateRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            try {
                repository.updateRecipe(recipe)
                _events.send(RecipeEvent.RecipeUpdated)
            } catch (e: Exception) {
                _events.send(RecipeEvent.ShowMessage(e.message ?: "Error al actualizar la receta"))
            }
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            try {
                repository.deleteRecipe(recipe)
                _events.send(RecipeEvent.RecipeDeleted)
            } catch (e: Exception) {
                _events.send(RecipeEvent.ShowMessage(e.message ?: "Error al eliminar la receta"))
            }
        }
    }
}

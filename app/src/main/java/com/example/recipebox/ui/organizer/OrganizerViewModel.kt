package com.example.recipebox.ui.organizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipebox.data.model.Category
import com.example.recipebox.data.model.Ingredient
import com.example.recipebox.repository.CategoryRepository
import com.example.recipebox.repository.IngredientRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class OrganizerViewModel(
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedTab = MutableStateFlow(OrganizerTab.CATEGORIES)
    private val _formState = MutableStateFlow(OrganizerUiState())

    private val categoriesFlow = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            categoryRepository.observeCategories()
        } else {
            categoryRepository.searchCategories(query)
        }
    }

    private val ingredientsFlow = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            ingredientRepository.observeIngredients()
        } else {
            ingredientRepository.searchIngredients(query)
        }
    }

    val uiState: StateFlow<OrganizerUiState> = combine(
        _selectedTab,
        _searchQuery,
        categoriesFlow,
        ingredientsFlow,
        _formState
    ) { tab, query, categories, ingredients, form ->
        form.copy(
            selectedTab = tab,
            searchQuery = query,
            categories = categories,
            ingredients = ingredients
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = OrganizerUiState()
    )

    fun onTabSelected(tab: OrganizerTab) {
        _selectedTab.value = tab
        clearError()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // --- Eventos de Categorías ---

    fun openNewCategoryDialog() {
        _formState.update {
            it.copy(
                isCategoryDialogOpen = true,
                editingCategoryId = null,
                categoryNameInput = "",
                categoryDescriptionInput = "",
                errorMessage = null
            )
        }
    }

    fun openEditCategoryDialog(category: Category) {
        _formState.update {
            it.copy(
                isCategoryDialogOpen = true,
                editingCategoryId = category.id,
                categoryNameInput = category.name,
                categoryDescriptionInput = category.description,
                errorMessage = null
            )
        }
    }

    fun dismissCategoryDialog() {
        _formState.update {
            it.copy(
                isCategoryDialogOpen = false,
                editingCategoryId = null,
                categoryNameInput = "",
                categoryDescriptionInput = "",
                errorMessage = null
            )
        }
    }

    fun onCategoryNameChange(name: String) {
        _formState.update { it.copy(categoryNameInput = name, errorMessage = null) }
    }

    fun onCategoryDescriptionChange(description: String) {
        _formState.update { it.copy(categoryDescriptionInput = description, errorMessage = null) }
    }

    fun saveCategory() {
        val current = _formState.value
        if (!current.isCategoryInputValid) {
            _formState.update { it.copy(errorMessage = "El nombre de la categoría es obligatorio") }
            return
        }

        viewModelScope.launch {
            val category = Category(
                id = current.editingCategoryId ?: 0L,
                name = current.categoryNameInput.trim(),
                description = current.categoryDescriptionInput.trim()
            )
            val result = if (current.editingCategoryId == null) {
                categoryRepository.saveCategory(category)
            } else {
                categoryRepository.updateCategory(category)
            }

            result.onSuccess {
                dismissCategoryDialog()
            }.onFailure {
                _formState.update { state ->
                    state.copy(errorMessage = "Error: Ya existe una categoría con ese nombre o no se pudo guardar")
                }
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category).onFailure {
                _formState.update { state ->
                    state.copy(errorMessage = "No se puede eliminar la categoría porque está en uso")
                }
            }
        }
    }

    // --- Eventos de Ingredientes ---

    fun openNewIngredientDialog() {
        _formState.update {
            it.copy(
                isIngredientDialogOpen = true,
                editingIngredientId = null,
                ingredientNameInput = "",
                ingredientUnitInput = "g",
                errorMessage = null
            )
        }
    }

    fun openEditIngredientDialog(ingredient: Ingredient) {
        _formState.update {
            it.copy(
                isIngredientDialogOpen = true,
                editingIngredientId = ingredient.id,
                ingredientNameInput = ingredient.name,
                ingredientUnitInput = ingredient.defaultUnit,
                errorMessage = null
            )
        }
    }

    fun dismissIngredientDialog() {
        _formState.update {
            it.copy(
                isIngredientDialogOpen = false,
                editingIngredientId = null,
                ingredientNameInput = "",
                ingredientUnitInput = "g",
                errorMessage = null
            )
        }
    }

    fun onIngredientNameChange(name: String) {
        _formState.update { it.copy(ingredientNameInput = name, errorMessage = null) }
    }

    fun onIngredientUnitChange(unit: String) {
        _formState.update { it.copy(ingredientUnitInput = unit, errorMessage = null) }
    }

    fun saveIngredient() {
        val current = _formState.value
        if (!current.isIngredientInputValid) {
            _formState.update { it.copy(errorMessage = "Nombre y unidad son obligatorios") }
            return
        }

        viewModelScope.launch {
            val ingredient = Ingredient(
                id = current.editingIngredientId ?: 0L,
                name = current.ingredientNameInput.trim(),
                defaultUnit = current.ingredientUnitInput.trim()
            )
            val result = if (current.editingIngredientId == null) {
                ingredientRepository.saveIngredient(ingredient)
            } else {
                ingredientRepository.updateIngredient(ingredient)
            }

            result.onSuccess {
                dismissIngredientDialog()
            }.onFailure {
                _formState.update { state ->
                    state.copy(errorMessage = "Error: Ya existe ese ingrediente o no se pudo guardar")
                }
            }
        }
    }

    fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientRepository.deleteIngredient(ingredient).onFailure {
                _formState.update { state ->
                    state.copy(errorMessage = "No se puede eliminar un ingrediente que está en uso en una receta")
                }
            }
        }
    }

    fun clearError() {
        _formState.update { it.copy(errorMessage = null) }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        fun provideFactory(
            categoryRepository: CategoryRepository,
            ingredientRepository: IngredientRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                OrganizerViewModel(
                    categoryRepository = categoryRepository,
                    ingredientRepository = ingredientRepository
                )
            }
        }
    }
}
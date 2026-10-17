package com.example.recipebox.ui.viewmodels

import com.example.recipebox.data.model.Category
import com.example.recipebox.data.model.Ingredient

enum class OrganizerTab {
    CATEGORIES,
    INGREDIENTS
}

/**
 * Estado inmutable de la IU para el módulo de Categorías e Ingredientes.
 */
data class OrganizerUiState(
    val selectedTab: OrganizerTab = OrganizerTab.CATEGORIES,
    val searchQuery: String = "",
    val categories: List<Category> = emptyList(),
    val ingredients: List<Ingredient> = emptyList(),
    // Estado del diálogo / formulario de Categoría
    val isCategoryDialogOpen: Boolean = false,
    val editingCategoryId: Long? = null,
    val categoryNameInput: String = "",
    val categoryDescriptionInput: String = "",
    // Estado del diálogo / formulario de Ingrediente
    val isIngredientDialogOpen: Boolean = false,
    val editingIngredientId: Long? = null,
    val ingredientNameInput: String = "",
    val ingredientUnitInput: String = "g",
    // Mensajes de error o validación defensiva
    val errorMessage: String? = null
) {
    val isCategoryInputValid: Boolean
        get() = categoryNameInput.trim().isNotBlank()

    val isIngredientInputValid: Boolean
        get() = ingredientNameInput.trim().isNotBlank() && ingredientUnitInput.trim().isNotBlank()
}
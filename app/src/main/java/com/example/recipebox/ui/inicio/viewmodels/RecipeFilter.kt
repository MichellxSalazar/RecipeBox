package com.example.recipebox.ui.inicio.viewmodels

import com.example.recipebox.data.local.entity.RecipeEntity

// Utilidad para filtrar y ordenar recetas en la pantalla de inicio
object RecipeFilter {

    fun filterAndSort(
        recipes: List<RecipeEntity>,
        searchText: String,
        onlyFavorites: Boolean,
        selectedCategoryId: Long,
        selectedOrder: String
    ): List<RecipeEntity> {

        val filtered = recipes.filter { recipe ->
            val matchesSearch = recipe.name.contains(
                searchText.trim(),
                ignoreCase = true
            )

            val matchesFavorites =
                !onlyFavorites || recipe.isFavorite

            val matchesCategory =
                selectedCategoryId == 0L ||
                        recipe.categoryId == selectedCategoryId

            matchesSearch && matchesFavorites && matchesCategory
        }

        return when (selectedOrder) {
            "az" -> filtered.sortedBy { it.name.lowercase() }
            "za" -> filtered.sortedByDescending { it.name.lowercase() }
            "fast" -> filtered.sortedBy { it.prepTimeMinutes }
            "slow" -> filtered.sortedByDescending { it.prepTimeMinutes }
            else -> filtered
        }
    }

    // Sobrecarga para compatibilidad con parámetros Int
    fun filterAndSort(
        recipes: List<RecipeEntity>,
        searchText: String,
        onlyFavorites: Boolean,
        selectedCategoryId: Int,
        selectedOrder: String
    ): List<RecipeEntity> = filterAndSort(
        recipes = recipes,
        searchText = searchText,
        onlyFavorites = onlyFavorites,
        selectedCategoryId = selectedCategoryId.toLong(),
        selectedOrder = selectedOrder
    )
}

package com.example.recipebox.ui.inicio.viewmodels

import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.RecipeEntity

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val recipes: List<RecipeEntity>,
        val categories: List<CategoryEntity> = emptyList()
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}

package com.example.recipebox.ui.inicio.viewmodels

import com.example.recipebox.data.local.entity.RecipeEntity

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val recipes: List<RecipeEntity>
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}

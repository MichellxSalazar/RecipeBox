package com.example.recipebox.ui.viewmodels

import com.example.recipebox.data.local.entity.RecipeEntity

sealed interface RecipeUiState {
    object Loading : RecipeUiState
    data class Success(val recipes: List<RecipeEntity>) : RecipeUiState
    data class Error(val message: String) : RecipeUiState
}

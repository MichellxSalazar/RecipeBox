package com.example.recipebox.ui.detail

import com.example.recipebox.data.local.RecipeEntity

data class RecipeDetailUiState(
    val isLoading: Boolean = true,
    val recipe: RecipeEntity? = null,
    val currentStepIndex: Int = 0,
    val isKeepScreenOn: Boolean = true,
    val errorMessage: String? = null
)
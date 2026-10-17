package com.example.recipebox.ui.detail

import com.example.recipebox.data.local.entity.RecipeEntity

data class RecipeDetailUiState(
    val isLoading: Boolean = true,
    val recipe: RecipeEntity? = null,
    val title: String = "",
    val description: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val currentStepIndex: Int = 0,
    val isKeepScreenOn: Boolean = true,
    val errorMessage: String? = null
)
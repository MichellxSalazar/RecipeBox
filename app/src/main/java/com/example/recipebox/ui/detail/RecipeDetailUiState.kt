package com.example.recipebox.ui.detail

data class RecipeDetailUiState(
    val isLoading: Boolean = true,
    val title: String = "",
    val description: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val currentStepIndex: Int = 0,
    val isKeepScreenOn: Boolean = true,
    val errorMessage: String? = null
)
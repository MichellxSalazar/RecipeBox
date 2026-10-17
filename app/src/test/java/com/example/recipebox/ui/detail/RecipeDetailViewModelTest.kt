package com.example.recipebox.ui.detail

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class RecipeDetailViewModelTest {

    private lateinit var viewModel: RecipeDetailViewModel

    @Before
    fun setUp() {
        viewModel = RecipeDetailViewModel()
    }

    @Test
    fun loadRecipe_updatesStateWithRecipeData() {
        viewModel.loadRecipe(1)
        val currentState = viewModel.uiState.value

        assertFalse(currentState.isLoading)
        assertEquals("Pasta Carbonara Tradicional", currentState.title)
        assertEquals(5, currentState.ingredients.size)
    }

    @Test
    fun nextStep_incrementsCurrentStepIndex() {
        viewModel.loadRecipe(1)
        viewModel.nextStep()

        assertEquals(1, viewModel.uiState.value.currentStepIndex)
    }

    @Test
    fun previousStep_decrementsCurrentStepIndex() {
        viewModel.loadRecipe(1)
        viewModel.nextStep() // Pasa al paso 1
        viewModel.previousStep() // Regresa al paso 0

        assertEquals(0, viewModel.uiState.value.currentStepIndex)
    }
}
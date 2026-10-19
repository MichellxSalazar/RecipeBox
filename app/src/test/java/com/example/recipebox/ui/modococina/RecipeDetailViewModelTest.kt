package com.example.recipebox.ui.modococina

import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.repository.RecipeRepository
import com.example.recipebox.ui.modococina.viewmodels.RecipeDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: RecipeDetailViewModel

    // Repositorio fake para pruebas unitarias
    private class FakeRecipeRepository : RecipeRepository {
        override fun getAllRecipes(): Flow<List<RecipeEntity>> = emptyFlow()
        override fun getRecipesByCategory(categoryId: Long): Flow<List<RecipeEntity>> = emptyFlow()
        override suspend fun getRecipeById(id: Long): RecipeEntity? = null
        override suspend fun insertRecipe(recipe: RecipeEntity) {}
        override suspend fun updateRecipe(recipe: RecipeEntity) {}
        override suspend fun deleteRecipe(recipe: RecipeEntity) {}
        override suspend fun getCategoryCount(): Int = 0
        override suspend fun insertCategories(categories: List<CategoryEntity>) {}
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipeDetailViewModel(FakeRecipeRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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

package com.example.recipebox.repository

import com.example.recipebox.data.local.dao.RecipeDao
import com.example.recipebox.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Hilt inyecta RecipeDao; el constructor lleva @Inject para que AppModule lo pueda proveer
class RecipeRepository @Inject constructor(private val recipeDao: RecipeDao) {

    fun getAllRecipes(): Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()

    fun getRecipesByCategory(categoryId: Long): Flow<List<RecipeEntity>> =
        recipeDao.getRecipesByCategory(categoryId)

    suspend fun getRecipeById(id: Long): RecipeEntity? = recipeDao.getRecipeById(id)

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: RecipeEntity) {
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: RecipeEntity) {
        recipeDao.deleteRecipe(recipe)
    }

    // --- Métodos auxiliares usados por RecetasViewModel ---

    suspend fun getCategoryCount(): Int {
        // RecetasViewModel usa esto para pre-poblar categorías por defecto
        return 0 // Se implementa a través de CategoryRepository en producción
    }

    suspend fun insertCategories(categories: List<com.example.recipebox.data.local.entity.CategoryEntity>) {
        // No-op: las categorías se manejan a través de CategoryRepository y AppModule
        // Este método existe para compatibilidad con RecetasViewModel
    }
}

package com.example.recipebox.repository

import com.example.recipebox.data.local.dao.RecipeDao
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Implementación concreta de RecipeRepository que interactúa con Room DAO
class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override fun getAllRecipes(): Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()

    override fun getRecipesByCategory(categoryId: Long): Flow<List<RecipeEntity>> =
        recipeDao.getRecipesByCategory(categoryId)

    override suspend fun getRecipeById(id: Long): RecipeEntity? = recipeDao.getRecipeById(id)

    override suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    override suspend fun updateRecipe(recipe: RecipeEntity) {
        recipeDao.updateRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: RecipeEntity) {
        recipeDao.deleteRecipe(recipe)
    }

    override suspend fun getCategoryCount(): Int {
        return 0
    }

    override suspend fun insertCategories(categories: List<CategoryEntity>) {
        // No-op para compatibilidad
    }
}

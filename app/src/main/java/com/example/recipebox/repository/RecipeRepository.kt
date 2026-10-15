package com.example.recipebox.repository

import com.example.recipebox.data.local.dao.RecipeDao
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    fun getAllRecipes(): Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()

    fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeEntity>> =
        recipeDao.getRecipesByCategory(categoryId)

    suspend fun getRecipeById(id: Int): RecipeEntity? = recipeDao.getRecipeById(id)

    suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: RecipeEntity) {
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: RecipeEntity) {
        recipeDao.deleteRecipe(recipe)
    }

    suspend fun insertCategories(categories: List<CategoryEntity>) {
        recipeDao.insertCategories(categories)
    }

    suspend fun getCategoryCount(): Int = recipeDao.getCategoryCount()
}

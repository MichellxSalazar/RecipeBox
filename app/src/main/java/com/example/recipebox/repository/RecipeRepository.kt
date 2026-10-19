package com.example.recipebox.repository

import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

// Interfaz del repositorio de recetas (Single Source of Truth)
interface RecipeRepository {
    fun getAllRecipes(): Flow<List<RecipeEntity>>
    fun getRecipesByCategory(categoryId: Long): Flow<List<RecipeEntity>>
    suspend fun getRecipeById(id: Long): RecipeEntity?
    suspend fun insertRecipe(recipe: RecipeEntity)
    suspend fun updateRecipe(recipe: RecipeEntity)
    suspend fun deleteRecipe(recipe: RecipeEntity)
    suspend fun getCategoryCount(): Int
    suspend fun insertCategories(categories: List<CategoryEntity>)
}

package com.example.recipebox.repository

import com.example.recipebox.data.model.Ingredient
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {
    fun observeIngredients(): Flow<List<Ingredient>>
    fun searchIngredients(query: String): Flow<List<Ingredient>>
    suspend fun getIngredientById(id: Long): Ingredient?
    suspend fun saveIngredient(ingredient: Ingredient): Result<Long>
    suspend fun updateIngredient(ingredient: Ingredient): Result<Unit>
    suspend fun deleteIngredient(ingredient: Ingredient): Result<Unit>
}
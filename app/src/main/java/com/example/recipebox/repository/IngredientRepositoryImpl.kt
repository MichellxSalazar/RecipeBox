package com.example.recipebox.repository

import com.example.recipebox.data.local.dao.IngredientDao
import com.example.recipebox.data.mapper.toDomainModel
import com.example.recipebox.data.mapper.toEntity
import com.example.recipebox.data.model.Ingredient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao
) : IngredientRepository {

    override fun observeIngredients(): Flow<List<Ingredient>> =
        ingredientDao.observeIngredients().map { entities ->
            entities.map { it.toDomainModel() }
        }

    override fun searchIngredients(query: String): Flow<List<Ingredient>> =
        ingredientDao.searchIngredients(query.trim()).map { entities ->
            entities.map { it.toDomainModel() }
        }

    override suspend fun getIngredientById(id: Long): Ingredient? =
        ingredientDao.getIngredientById(id)?.toDomainModel()

    override suspend fun saveIngredient(ingredient: Ingredient): Result<Long> = runCatching {
        require(ingredient.name.isNotBlank()) { "El nombre del ingrediente no puede estar vacío" }
        ingredientDao.insertIngredient(ingredient.toEntity())
    }

    override suspend fun updateIngredient(ingredient: Ingredient): Result<Unit> = runCatching {
        require(ingredient.name.isNotBlank()) { "El nombre del ingrediente no puede estar vacío" }
        ingredientDao.updateIngredient(ingredient.toEntity())
    }

    override suspend fun deleteIngredient(ingredient: Ingredient): Result<Unit> = runCatching {
        ingredientDao.deleteIngredient(ingredient.toEntity())
    }
}
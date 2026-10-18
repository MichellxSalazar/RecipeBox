package com.example.recipebox.repository

import com.example.recipebox.data.local.dao.CategoryDao
import com.example.recipebox.data.mapper.toDomainModel
import com.example.recipebox.data.mapper.toEntity
import com.example.recipebox.data.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun observeCategories(): Flow<List<Category>> =
        categoryDao.observeCategories().map { entities ->
            entities.map { it.toDomainModel() }
        }

    override fun searchCategories(query: String): Flow<List<Category>> =
        categoryDao.searchCategories(query.trim()).map { entities ->
            entities.map { it.toDomainModel() }
        }

    override suspend fun getCategoryById(id: Long): Category? =
        categoryDao.getCategoryById(id)?.toDomainModel()

    override suspend fun saveCategory(category: Category): Result<Long> = runCatching {
        require(category.name.isNotBlank()) { "El nombre de la categoría no puede estar vacío" }
        categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun updateCategory(category: Category): Result<Unit> = runCatching {
        require(category.name.isNotBlank()) { "El nombre de la categoría no puede estar vacío" }
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category): Result<Unit> = runCatching {
        categoryDao.deleteCategory(category.toEntity())
    }
}
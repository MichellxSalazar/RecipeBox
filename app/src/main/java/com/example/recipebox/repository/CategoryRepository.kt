package com.example.recipebox.repository

import com.example.recipebox.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeCategories(): Flow<List<Category>>
    fun searchCategories(query: String): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun saveCategory(category: Category): Result<Long>
    suspend fun updateCategory(category: Category): Result<Unit>
    suspend fun deleteCategory(category: Category): Result<Unit>
}
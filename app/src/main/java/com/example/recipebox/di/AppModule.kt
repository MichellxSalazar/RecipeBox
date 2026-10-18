package com.example.recipebox.di

import android.content.Context
import com.example.recipebox.data.local.RecipeDatabase
import com.example.recipebox.data.local.dao.RecipeDao
import com.example.recipebox.data.local.dao.CategoryDao
import com.example.recipebox.data.local.dao.IngredientDao
import com.example.recipebox.repository.RecipeRepository
import com.example.recipebox.repository.CategoryRepository
import com.example.recipebox.repository.CategoryRepositoryImpl
import com.example.recipebox.repository.IngredientRepository
import com.example.recipebox.repository.IngredientRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RecipeDatabase {
        return RecipeDatabase.getDatabase(context)
    }

    @Provides
    fun provideRecipeDao(
        database: RecipeDatabase
    ): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    fun provideCategoryDao(
        database: RecipeDatabase
    ): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideIngredientDao(
        database: RecipeDatabase
    ): IngredientDao {
        return database.ingredientDao()
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
        recipeDao: RecipeDao
    ): RecipeRepository {
        return RecipeRepository(recipeDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryDao: CategoryDao
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }

    @Provides
    @Singleton
    fun provideIngredientRepository(
        ingredientDao: IngredientDao
    ): IngredientRepository {
        return IngredientRepositoryImpl(ingredientDao)
    }
}
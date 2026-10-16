package com.example.recipebox.di

import android.content.Context
import androidx.room.Room
import com.example.recipebox.data.local.RecipeDatabase
import com.example.recipebox.data.local.dao.RecipeDao
import com.example.recipebox.repository.RecipeRepository
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
    @Singleton
    fun provideRecipeRepository(
        recipeDao: RecipeDao
    ): RecipeRepository {
        return RecipeRepository(recipeDao)
    }
}

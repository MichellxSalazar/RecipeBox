package com.example.recipebox.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipebox.data.local.dao.RecipeDao
import com.example.recipebox.data.local.dao.CategoryDao
import com.example.recipebox.data.local.dao.IngredientDao
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.IngredientEntity
import com.example.recipebox.data.local.entity.RecipeIngredientCrossRef

@Database(
    entities = [
        RecipeEntity::class, 
        CategoryEntity::class, 
        IngredientEntity::class, 
        RecipeIngredientCrossRef::class
    ], 
    version = 1, 
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun ingredientDao(): IngredientDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
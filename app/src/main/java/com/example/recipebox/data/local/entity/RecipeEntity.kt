package com.example.recipebox.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val categoryId: Int,
    val ingredients: String, // Can be a JSON string or related to another entity
    val instructions: String,
    val prepTimeMinutes: Int,
    val isFavorite: Boolean = false
)

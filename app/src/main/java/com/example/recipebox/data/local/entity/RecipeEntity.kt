package com.example.recipebox.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipes",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val categoryId: Long,
    val ingredients: String, // Cadena de texto con los ingredientes de la receta
    val instructions: String,
    val prepTimeMinutes: Int,
    val isFavorite: Boolean = false
)

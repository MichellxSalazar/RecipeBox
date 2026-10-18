package com.example.recipebox.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "recipe_ingredient_cross_ref",
    primaryKeys = ["recipe_id", "ingredient_id"],
    indices = [
        Index(value = ["recipe_id"]),
        Index(value = ["ingredient_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = IngredientEntity::class,
            parentColumns = ["id"],
            childColumns = ["ingredient_id"],
            onDelete = ForeignKey.RESTRICT // Evita borrar un ingrediente si está en uso en una receta
        )
    ]
)
data class RecipeIngredientCrossRef(
    @ColumnInfo(name = "recipe_id")
    val recipeId: Long,
    @ColumnInfo(name = "ingredient_id")
    val ingredientId: Long,
    @ColumnInfo(name = "quantity")
    val quantity: Double,
    @ColumnInfo(name = "unit")
    val unit: String
)
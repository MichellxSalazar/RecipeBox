package com.example.recipebox.data.mapper

import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.IngredientEntity
import com.example.recipebox.data.model.Category
import com.example.recipebox.data.model.Ingredient

// Mappers de Categoría
fun CategoryEntity.toDomainModel(): Category = Category(
    id = id,
    name = name,
    description = description
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name.trim(),
    description = description.trim()
)

// Mappers de Ingrediente
fun IngredientEntity.toDomainModel(): Ingredient = Ingredient(
    id = id,
    name = name,
    defaultUnit = defaultUnit
)

fun Ingredient.toEntity(): IngredientEntity = IngredientEntity(
    id = id,
    name = name.trim(),
    defaultUnit = defaultUnit.trim()
)
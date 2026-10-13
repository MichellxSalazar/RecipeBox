package com.example.recipebox.data.model

/**
 * Modelo de dominio inmutable para representar un Ingrediente y su unidad por defecto.
 */
data class Ingredient(
    val id: Long = 0,
    val name: String,
    val defaultUnit: String = "g"
)
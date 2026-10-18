package com.example.recipebox.data.model

/**
 * Modelo de dominio inmutable para representar una Categoría en la lógica de la app.
 */
data class Category(
    val id: Long = 0,
    val name: String,
    val description: String = ""
)
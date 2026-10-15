package com.example.recipebox

import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.ui.home.RecipeFilter
import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeFilterTest {

    private val recipes = listOf(
        RecipeEntity(
            id = 1,
            name = "Arroz con pollo",
            categoryId = 1,
            ingredients = "Arroz, pollo",
            instructions = "Cocinar",
            prepTimeMinutes = 40,
            isFavorite = true
        ),
        RecipeEntity(
            id = 2,
            name = "Ensalada de atún",
            categoryId = 2,
            ingredients = "Atún, vegetales",
            instructions = "Mezclar",
            prepTimeMinutes = 15,
            isFavorite = false
        ),
        RecipeEntity(
            id = 3,
            name = "Pasta con queso",
            categoryId = 1,
            ingredients = "Pasta, queso",
            instructions = "Cocinar",
            prepTimeMinutes = 25,
            isFavorite = true
        )
    )

    private fun filter(
        search: String = "",
        favorites: Boolean = false,
        category: Int = 0,
        order: String = "default"
    ) = RecipeFilter.filterAndSort(
        recipes = recipes,
        searchText = search,
        onlyFavorites = favorites,
        selectedCategoryId = category,
        selectedOrder = order
    )

    @Test
    fun searchByName() {
        val result = filter(search = "arroz")
        assertEquals(listOf("Arroz con pollo"), result.map { it.name })
    }

    @Test
    fun searchIgnoresCaseAndSpaces() {
        val result = filter(search = "  ARROZ  ")
        assertEquals(1, result.size)
    }

    @Test
    fun filterFavorites() {
        val result = filter(favorites = true)
        assertEquals(2, result.size)
    }

    @Test
    fun filterByCategory() {
        val result = filter(category = 1)
        assertEquals(2, result.size)
    }

    @Test
    fun sortByNameAscending() {
        val result = filter(order = "az")
        assertEquals(
            listOf("Arroz con pollo", "Ensalada de atún", "Pasta con queso"),
            result.map { it.name }
        )
    }

    @Test
    fun sortByFastest() {
        val result = filter(order = "fast")
        assertEquals(
            listOf(15, 25, 40),
            result.map { it.prepTimeMinutes }
        )
    }

    @Test
    fun noResults() {
        val result = filter(search = "Pizza")
        assertEquals(0, result.size)
    }



    @Test
    fun sortByNameDescending() {
        val result = filter(order = "za")

        assertEquals(
            listOf(
                "Pasta con queso",
                "Ensalada de atún",
                "Arroz con pollo"
            ),
            result.map { it.name }
        )
    }

    @Test
    fun sortByLongestTime() {
        val result = filter(order = "slow")

        assertEquals(
            listOf(40, 25, 15),
            result.map { it.prepTimeMinutes }
        )
    }

    @Test
    fun combineCategoryAndFavorites() {
        val result = filter(
            category = 1,
            favorites = true
        )

        assertEquals(2, result.size)
        assertEquals(
            listOf("Arroz con pollo", "Pasta con queso"),
            result.map { it.name }
        )
    }

    @Test
    fun combineSearchAndCategoryWithoutResults() {
        val result = filter(
            search = "arroz",
            category = 2
        )

        assertEquals(0, result.size)
    }

}



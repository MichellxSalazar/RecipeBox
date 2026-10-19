package com.example.recipebox.data.local

import com.example.recipebox.data.local.dao.CategoryDao
import com.example.recipebox.data.local.dao.IngredientDao
import com.example.recipebox.data.local.dao.RecipeDao
import com.example.recipebox.data.local.entity.CategoryEntity
import com.example.recipebox.data.local.entity.IngredientEntity
import com.example.recipebox.data.local.entity.RecipeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Pre-pobla categorías, ingredientes y recetas iniciales en Room
object DatabasePrepopulator {

    val defaultCategories = listOf(
        CategoryEntity(id = 1L, name = "Desayunos", description = "Comienza el día con recetas energéticas y deliciosas"),
        CategoryEntity(id = 2L, name = "Almuerzos", description = "Platos fuertes y nutritivos para el mediodía"),
        CategoryEntity(id = 3L, name = "Cenas", description = "Opciones ligeras, saludables y reconfortantes"),
        CategoryEntity(id = 4L, name = "Postres", description = "Deliciosos dulces y bocados para consentirte")
    )

    val defaultIngredients = listOf(
        IngredientEntity(name = "Harina de trigo", defaultUnit = "g"),
        IngredientEntity(name = "Huevos", defaultUnit = "unidad"),
        IngredientEntity(name = "Leche entera", defaultUnit = "ml"),
        IngredientEntity(name = "Mantequilla", defaultUnit = "g"),
        IngredientEntity(name = "Azúcar", defaultUnit = "g"),
        IngredientEntity(name = "Pasta Spaghetti", defaultUnit = "g"),
        IngredientEntity(name = "Queso Parmesano", defaultUnit = "g"),
        IngredientEntity(name = "Tocineta / Guanciale", defaultUnit = "g"),
        IngredientEntity(name = "Pechuga de pollo", defaultUnit = "g"),
        IngredientEntity(name = "Lechuga romana", defaultUnit = "g"),
        IngredientEntity(name = "Arroz blanco", defaultUnit = "g"),
        IngredientEntity(name = "Café expreso", defaultUnit = "ml"),
        IngredientEntity(name = "Queso Mascarpone", defaultUnit = "g"),
        IngredientEntity(name = "Aceite de oliva", defaultUnit = "ml"),
        IngredientEntity(name = "Sal y Pimienta", defaultUnit = "g")
    )

    val defaultRecipes = listOf(
        RecipeEntity(
            id = 1L,
            name = "Pancakes Clásicos Americanos",
            categoryId = 1L,
            ingredients = "200 g de Harina de trigo, 2 unidades de Huevos, 250 ml de Leche entera, 30 g de Mantequilla, 30 g de Azúcar",
            instructions = "Paso 1: Mezclar en un tazón la harina y el azúcar.\nPaso 2: En otro recipiente, batir los huevos junto con la leche y la mantequilla derretida.\nPaso 3: Unir ambas mezclas suavemente con un batidor de mano sin batir en exceso.\nPaso 4: Calentar una sartén a fuego medio con un poco de mantequilla.\nPaso 5: Verter porciones de masa y cocinar 2 minutos por lado hasta que salgan burbujas y dorar.",
            prepTimeMinutes = 20,
            isFavorite = true
        ),
        RecipeEntity(
            id = 2L,
            name = "Pasta Carbonara Tradicional",
            categoryId = 2L,
            ingredients = "400 g de Pasta Spaghetti, 150 g de Tocineta / Guanciale, 4 unidades de Huevos, 100 g de Queso Parmesano, 5 g de Sal y Pimienta",
            instructions = "Paso 1: Poner a hervir abundante agua con sal en una olla grande y cocinar la pasta al dente.\nPaso 2: Dorar el guanciale o tocineta picado en cubos en una sartén a fuego medio sin agregar aceite.\nPaso 3: Mezclar las yemas de huevo con el queso parmesano y pimienta negra hasta formar una crema homogénea.\nPaso 4: Escurrir la pasta reservando un poco de agua de cocción.\nPaso 5: Mezclar la pasta con la tocineta fuera del fuego y verter la crema de huevo revolviendo rápidamente.",
            prepTimeMinutes = 25,
            isFavorite = true
        ),
        RecipeEntity(
            id = 3L,
            name = "Ensalada César con Pollo",
            categoryId = 3L,
            ingredients = "250 g de Pechuga de pollo, 150 g de Lechuga romana, 50 g de Queso Parmesano, 20 ml de Aceite de oliva, 5 g de Sal y Pimienta",
            instructions = "Paso 1: Salpimentar la pechuga de pollo y cocinarla a la plancha con unas gotas de aceite de oliva hasta dorar por ambos lados.\nPaso 2: Lavar, desinfectar y trocear la lechuga romana en un tazón grande.\nPaso 3: Cortar la pechuga de pollo cocida en tiras o cubos medianos.\nPaso 4: Mezclar la lechuga con el pollo y espolvorear virutas de queso parmesano por encima.",
            prepTimeMinutes = 15,
            isFavorite = false
        ),
        RecipeEntity(
            id = 4L,
            name = "Tiramisú Casero",
            categoryId = 4L,
            ingredients = "250 g de Queso Mascarpone, 3 unidades de Huevos, 100 g de Azúcar, 200 ml de Café expreso, 100 g de Harina de trigo",
            instructions = "Paso 1: Preparar el café expreso y dejar enfriar completamente.\nPaso 2: Separar las yemas de las claras. Batir las yemas con el azúcar hasta blanquear.\nPaso 3: Incorporar el queso mascarpone suavemente a la mezcla de yemas.\nPaso 4: Batir las claras a punto de nieve e integrarlas con movimientos envolventes.\nPaso 5: Remojar los bizcochos en el café, armar capas alternadas de bizcocho y crema, y refrigerar mínimo 4 horas.",
            prepTimeMinutes = 30,
            isFavorite = true
        ),
        RecipeEntity(
            id = 5L,
            name = "Arroz con Pollo Casero",
            categoryId = 2L,
            ingredients = "300 g de Arroz blanco, 350 g de Pechuga de pollo, 20 ml de Aceite de oliva, 5 g de Sal y Pimienta, 2 unidades de Huevos",
            instructions = "Paso 1: Dorar el pollo troceado con sal y pimienta en una olla mediana con aceite de oliva.\nPaso 2: Agregar el arroz y sofreír durante 2 minutos revolviendo constantemente.\nPaso 3: Añadir agua hirviendo (el doble del volumen de arroz) y cocinar a fuego alto hasta que hierva.\nPaso 4: Bajar el fuego al mínimo, tapar la olla y dejar cocinar por 20 minutos hasta que el arroz esté en su punto.",
            prepTimeMinutes = 40,
            isFavorite = false
        )
    )

    suspend fun prepopulateIfNeeded(
        recipeDao: RecipeDao,
        categoryDao: CategoryDao,
        ingredientDao: IngredientDao
    ) = withContext(Dispatchers.IO) {
        if (categoryDao.getCategoryCount() == 0) {
            categoryDao.insertCategories(defaultCategories)
        }
        if (ingredientDao.getIngredientCount() == 0) {
            ingredientDao.insertIngredients(defaultIngredients)
        }
        if (recipeDao.getRecipeCount() == 0) {
            recipeDao.insertRecipes(defaultRecipes)
        }
    }
}

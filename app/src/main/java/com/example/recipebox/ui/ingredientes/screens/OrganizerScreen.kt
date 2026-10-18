package com.example.recipebox.ui.ingredientes.screens

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recipebox.data.model.Category
import com.example.recipebox.data.model.Ingredient
import com.example.recipebox.ui.ingredientes.viewmodels.OrganizerTab
import com.example.recipebox.ui.ingredientes.viewmodels.OrganizerUiState
import com.example.recipebox.ui.ingredientes.viewmodels.OrganizerViewModel

@Composable
fun OrganizerRoute(
    viewModel: OrganizerViewModel,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    OrganizerScreen(
        uiState = uiState,
        onTabSelected = viewModel::onTabSelected,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onAddClick = {
            if (uiState.selectedTab == OrganizerTab.CATEGORIES) {
                viewModel.openNewCategoryDialog()
            } else {
                viewModel.openNewIngredientDialog()
            }
        },
        onEditCategory = viewModel::openEditCategoryDialog,
        onDeleteCategory = viewModel::deleteCategory,
        onCategoryNameChange = viewModel::onCategoryNameChange,
        onCategoryDescriptionChange = viewModel::onCategoryDescriptionChange,
        onSaveCategory = viewModel::saveCategory,
        onDismissCategoryDialog = viewModel::dismissCategoryDialog,
        onEditIngredient = viewModel::openEditIngredientDialog,
        onDeleteIngredient = viewModel::deleteIngredient,
        onIngredientNameChange = viewModel::onIngredientNameChange,
        onIngredientUnitChange = viewModel::onIngredientUnitChange,
        onSaveIngredient = viewModel::saveIngredient,
        onDismissIngredientDialog = viewModel::dismissIngredientDialog,
        modifier = modifier,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizerScreen(
    uiState: OrganizerUiState,
    onTabSelected: (OrganizerTab) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onEditCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onCategoryNameChange: (String) -> Unit,
    onCategoryDescriptionChange: (String) -> Unit,
    onSaveCategory: () -> Unit,
    onDismissCategoryDialog: () -> Unit,
    onEditIngredient: (Ingredient) -> Unit,
    onDeleteIngredient: (Ingredient) -> Unit,
    onIngredientNameChange: (String) -> Unit,
    onIngredientUnitChange: (String) -> Unit,
    onSaveIngredient: () -> Unit,
    onDismissIngredientDialog: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Categorías e Ingredientes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar elemento"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
                Tab(
                    selected = uiState.selectedTab == OrganizerTab.CATEGORIES,
                    onClick = { onTabSelected(OrganizerTab.CATEGORIES) },
                    text = { Text("Categorías (${uiState.categories.size})") }
                )
                Tab(
                    selected = uiState.selectedTab == OrganizerTab.INGREDIENTS,
                    onClick = { onTabSelected(OrganizerTab.INGREDIENTS) },
                    text = { Text("Ingredientes (${uiState.ingredients.size})") }
                )
            }

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                placeholder = {
                    Text(
                        if (uiState.selectedTab == OrganizerTab.CATEGORIES) {
                            "Buscar categoría..."
                        } else {
                            "Buscar ingrediente..."
                        }
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true
            )

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            when (uiState.selectedTab) {
                OrganizerTab.CATEGORIES -> {
                    CategoryListContent(
                        categories = uiState.categories,
                        onEditCategory = onEditCategory,
                        onDeleteCategory = onDeleteCategory,
                        modifier = Modifier.weight(1f)
                    )
                }
                OrganizerTab.INGREDIENTS -> {
                    IngredientListContent(
                        ingredients = uiState.ingredients,
                        onEditIngredient = onEditIngredient,
                        onDeleteIngredient = onDeleteIngredient,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (uiState.isCategoryDialogOpen) {
            CategoryFormDialog(
                isEditing = uiState.editingCategoryId != null,
                name = uiState.categoryNameInput,
                description = uiState.categoryDescriptionInput,
                isValid = uiState.isCategoryInputValid,
                errorMessage = uiState.errorMessage,
                onNameChange = onCategoryNameChange,
                onDescriptionChange = onCategoryDescriptionChange,
                onConfirm = onSaveCategory,
                onDismiss = onDismissCategoryDialog
            )
        }

        if (uiState.isIngredientDialogOpen) {
            IngredientFormDialog(
                isEditing = uiState.editingIngredientId != null,
                name = uiState.ingredientNameInput,
                unit = uiState.ingredientUnitInput,
                isValid = uiState.isIngredientInputValid,
                errorMessage = uiState.errorMessage,
                onNameChange = onIngredientNameChange,
                onUnitChange = onIngredientUnitChange,
                onConfirm = onSaveIngredient,
                onDismiss = onDismissIngredientDialog
            )
        }
    }
}

@Composable
fun CategoryListContent(
    categories: List<Category>,
    onEditCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    if (categories.isEmpty()) {
        EmptyOrganizerState(
            message = "No hay categorías registradas. ¡Presiona + para crear la primera!",
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = categories, key = { it.id }) { category ->
                CategoryItemCard(
                    category = category,
                    onEditClick = { onEditCategory(category) },
                    onDeleteClick = { onDeleteCategory(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryItemCard(
    category: Category,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (category.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar categoría"
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar categoría",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun IngredientListContent(
    ingredients: List<Ingredient>,
    onEditIngredient: (Ingredient) -> Unit,
    onDeleteIngredient: (Ingredient) -> Unit,
    modifier: Modifier = Modifier
) {
    if (ingredients.isEmpty()) {
        EmptyOrganizerState(
            message = "No hay ingredientes registrados. ¡Presiona + para agregar el primero!",
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = ingredients, key = { it.id }) { ingredient ->
                IngredientItemCard(
                    ingredient = ingredient,
                    onEditClick = { onEditIngredient(ingredient) },
                    onDeleteClick = { onDeleteIngredient(ingredient) }
                )
            }
        }
    }
}

@Composable
fun IngredientItemCard(
    ingredient: Ingredient,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Unidad base: ${ingredient.defaultUnit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar ingrediente"
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar ingrediente",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun EmptyOrganizerState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CategoryFormDialog(
    isEditing: Boolean,
    name: String,
    description: String,
    isValid: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (isEditing) "Editar categoría" else "Nueva categoría")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre de la categoría") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = isValid
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun IngredientFormDialog(
    isEditing: Boolean,
    name: String,
    unit: String,
    isValid: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (isEditing) "Editar ingrediente" else "Nuevo ingrediente")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre del ingrediente") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = onUnitChange,
                    label = { Text("Unidad (ej. g, ml, taza, unidad)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = isValid
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true, name = "Vista Previa - Categorías")
@Composable
private fun OrganizerScreenCategoriesPreview() {
    MaterialTheme {
        OrganizerScreen(
            uiState = OrganizerUiState(
                selectedTab = OrganizerTab.CATEGORIES,
                categories = listOf(
                    Category(id = 1, name = "Postres", description = "Recetas dulces y repostería"),
                    Category(id = 2, name = "Pastas", description = "Platos principales italianos"),
                    Category(id = 3, name = "Ensaladas", description = "Opciones frescas y ligeras")
                )
            ),
            onTabSelected = {},
            onSearchQueryChange = {},
            onAddClick = {},
            onEditCategory = {},
            onDeleteCategory = {},
            onCategoryNameChange = {},
            onCategoryDescriptionChange = {},
            onSaveCategory = {},
            onDismissCategoryDialog = {},
            onEditIngredient = {},
            onDeleteIngredient = {},
            onIngredientNameChange = {},
            onIngredientUnitChange = {},
            onSaveIngredient = {},
            onDismissIngredientDialog = {}
        )
    }
}

@Preview(showBackground = true, name = "Vista Previa - Ingredientes")
@Composable
private fun OrganizerScreenIngredientsPreview() {
    MaterialTheme {
        OrganizerScreen(
            uiState = OrganizerUiState(
                selectedTab = OrganizerTab.INGREDIENTS,
                ingredients = listOf(
                    Ingredient(id = 1, name = "Harina de trigo", defaultUnit = "g"),
                    Ingredient(id = 2, name = "Leche entera", defaultUnit = "ml"),
                    Ingredient(id = 3, name = "Huevos", defaultUnit = "unidad")
                )
            ),
            onTabSelected = {},
            onSearchQueryChange = {},
            onAddClick = {},
            onEditCategory = {},
            onDeleteCategory = {},
            onCategoryNameChange = {},
            onCategoryDescriptionChange = {},
            onSaveCategory = {},
            onDismissCategoryDialog = {},
            onEditIngredient = {},
            onDeleteIngredient = {},
            onIngredientNameChange = {},
            onIngredientUnitChange = {},
            onSaveIngredient = {},
            onDismissIngredientDialog = {}
        )
    }
}

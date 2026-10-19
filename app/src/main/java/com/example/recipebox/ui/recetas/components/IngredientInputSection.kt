package com.example.recipebox.ui.recetas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recipebox.data.model.Ingredient

data class RecipeIngredientItem(
    val amount: String,
    val unit: String,
    val name: String
) {
    fun toDisplayText(): String {
        val trimmedAmount = amount.trim()
        val trimmedUnit = unit.trim()
        val trimmedName = name.trim()
        return if (trimmedAmount.isNotEmpty() && trimmedUnit.isNotEmpty()) {
            "$trimmedAmount $trimmedUnit de $trimmedName"
        } else if (trimmedAmount.isNotEmpty()) {
            "$trimmedAmount $trimmedName"
        } else {
            trimmedName
        }
    }

    companion object {
        fun fromLine(line: String): RecipeIngredientItem {
            val trimmed = line.trim()
            val regex = Regex("""^(\d+(?:[.,]\d+)?)\s*([a-zA-ZáéíóúÁÉÍÓÚñÑ]+)?(?:\s+(?:de\s+)?(.+))?$""")
            val match = regex.find(trimmed)
            return if (match != null) {
                val amt = match.groupValues[1]
                val un = match.groupValues[2]
                val nm = match.groupValues[3].ifEmpty { trimmed }
                RecipeIngredientItem(amount = amt, unit = un, name = nm)
            } else {
                RecipeIngredientItem(amount = "", unit = "", name = trimmed)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientInputSection(
    ingredientsText: String,
    onIngredientsTextChange: (String) -> Unit,
    availableIngredients: List<Ingredient>,
    onSaveNewIngredient: (name: String, unit: String, onComplete: (Ingredient?) -> Unit) -> Unit,
    useMetricSystem: Boolean = true,
    modifier: Modifier = Modifier
) {
    var isManualTextMode by rememberSaveable { mutableStateOf(false) }

    // Parsear ingredientes actuales
    val itemsList = remember(ingredientsText) {
        if (ingredientsText.isBlank()) emptyList()
        else {
            ingredientsText.split("\n", ",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { RecipeIngredientItem.fromLine(it) }
        }
    }

    // Campos para nuevo ingrediente
    var ingredientNameInput by rememberSaveable { mutableStateOf("") }
    var ingredientAmountInput by rememberSaveable { mutableStateOf("") }
    var ingredientUnitInput by rememberSaveable(useMetricSystem) {
        mutableStateOf(if (useMetricSystem) "g" else "oz")
    }

    var expandedDropdown by remember { mutableStateOf(false) }
    var expandedUnitDropdown by remember { mutableStateOf(false) }

    val commonUnits = if (useMetricSystem) {
        listOf("g", "kg", "ml", "l", "unidades", "tazas", "cda", "cdta", "al gusto")
    } else {
        listOf("oz", "lb", "fl oz", "tazas", "unidades", "cda", "cdta", "al gusto")
    }

    val filteredSavedIngredients = remember(ingredientNameInput, availableIngredients) {
        if (ingredientNameInput.isBlank()) {
            availableIngredients
        } else {
            availableIngredients.filter {
                it.name.contains(ingredientNameInput, ignoreCase = true)
            }
        }
    }

    val exactMatch = availableIngredients.any {
        it.name.equals(ingredientNameInput.trim(), ignoreCase = true)
    }

    fun updateIngredients(newItems: List<RecipeIngredientItem>) {
        val newText = newItems.joinToString("\n") { it.toDisplayText() }
        onIngredientsTextChange(newText)
    }

    fun addCurrentIngredient() {
        val nameTrimmed = ingredientNameInput.trim()
        if (nameTrimmed.isBlank()) return

        // Si no existe exactamente en disponibles, guardarlo en la base de datos automáticamente
        if (!exactMatch) {
            onSaveNewIngredient(nameTrimmed, ingredientUnitInput.trim().ifEmpty { "g" }) {}
        }

        val newItem = RecipeIngredientItem(
            amount = ingredientAmountInput.trim(),
            unit = ingredientUnitInput.trim(),
            name = nameTrimmed
        )
        updateIngredients(itemsList + newItem)

        // Limpiar campos
        ingredientNameInput = ""
        ingredientAmountInput = ""
        ingredientUnitInput = if (useMetricSystem) "g" else "oz"
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = { isManualTextMode = !isManualTextMode }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isManualTextMode) "Selector asistido" else "Editar texto")
            }
        }

        if (isManualTextMode) {
            OutlinedTextField(
                value = ingredientsText,
                onValueChange = onIngredientsTextChange,
                label = { Text("Lista de ingredientes (un ingrediente por línea o separados por coma)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )
        } else {
            // Lista de ingredientes añadidos
            if (itemsList.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsList.forEachIndexed { index, item ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Restaurant,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.toDisplayText(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val mutable = itemsList.toMutableList()
                                        mutable.removeAt(index)
                                        updateIngredients(mutable)
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Eliminar ingrediente",
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Aún no has agregado ingredientes. Selecciona o escribe uno abajo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Formulario interactivo para agregar ingrediente
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Selector desplegable / búsqueda de ingrediente
                    ExposedDropdownMenuBox(
                        expanded = expandedDropdown,
                        onExpandedChange = { expandedDropdown = !expandedDropdown }
                    ) {
                        OutlinedTextField(
                            value = ingredientNameInput,
                            onValueChange = {
                                ingredientNameInput = it
                                expandedDropdown = true
                            },
                            label = { Text("Ingrediente (buscar o escribir nuevo)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true),
                            singleLine = true
                        )

                        if (expandedDropdown && (filteredSavedIngredients.isNotEmpty() || ingredientNameInput.isNotBlank())) {
                            ExposedDropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false }
                            ) {
                                // Sugerencia de crear nuevo si escribió algo que no existe
                                if (ingredientNameInput.isNotBlank() && !exactMatch) {
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    "Crear y agregar: '${ingredientNameInput.trim()}'",
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        onClick = {
                                            expandedDropdown = false
                                        }
                                    )
                                    HorizontalDivider()
                                }

                                filteredSavedIngredients.forEach { ingredient ->
                                    DropdownMenuItem(
                                        text = { Text(ingredient.name) },
                                        onClick = {
                                            ingredientNameInput = ingredient.name
                                            if (ingredient.defaultUnit.isNotBlank()) {
                                                ingredientUnitInput = ingredient.defaultUnit
                                            }
                                            expandedDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Fila de Cantidad y Unidad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ingredientAmountInput,
                            onValueChange = { ingredientAmountInput = it },
                            label = { Text("Cantidad") },
                            placeholder = { Text("ej. 200") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        ExposedDropdownMenuBox(
                            expanded = expandedUnitDropdown,
                            onExpandedChange = { expandedUnitDropdown = !expandedUnitDropdown },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = ingredientUnitInput,
                                onValueChange = { ingredientUnitInput = it },
                                label = { Text("Unidad") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnitDropdown) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true),
                                singleLine = true
                            )

                            ExposedDropdownMenu(
                                expanded = expandedUnitDropdown,
                                onDismissRequest = { expandedUnitDropdown = false }
                            ) {
                                commonUnits.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit) },
                                        onClick = {
                                            ingredientUnitInput = unit
                                            expandedUnitDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Botón para agregar el ingrediente a la receta
                    Button(
                        onClick = { addCurrentIngredient() },
                        enabled = ingredientNameInput.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (!exactMatch && ingredientNameInput.isNotBlank()) "Crear y agregar ingrediente" else "Agregar a la receta")
                    }
                }
            }
        }
    }
}

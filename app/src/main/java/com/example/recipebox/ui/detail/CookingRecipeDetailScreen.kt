package com.example.recipebox.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipebox.ui.theme.RecipeBoxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingRecipeDetailScreen(
    uiState: RecipeDetailUiState,
    onStartCookingClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val purplePrimary = Color(0xFF6750A4)
    val purpleContainer = Color(0xFFEADDFF)
    val onPurpleContainer = Color(0xFF21005D)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Receta", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = purplePrimary)
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarjeta Principal de la Receta con Borde
                item {
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = uiState.title.ifEmpty { "Receta sin título" },
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.description.ifEmpty { "Sin descripción disponible." },
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Sección Ingredientes
                item {
                    Text(
                        text = "Ingredientes",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = purplePrimary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Tarjetas de cada Ingrediente con Borde
                items(uiState.ingredients) { ingredient ->
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = purpleContainer,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "•",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = onPurpleContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = ingredient,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Botón Iniciar Modo Cocina
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onStartCookingClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = purplePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Iniciar Modo Cocina",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Modo Claro", showBackground = true)
@Composable
fun CookingRecipeDetailScreenPreview() {
    RecipeBoxTheme(darkTheme = false) {
        CookingRecipeDetailScreen(
            uiState = RecipeDetailUiState(
                isLoading = false,
                title = "Pasta Carbonara Tradicional",
                description = "Deliciosa receta italiana hecha a base de huevo, queso pecorino y guanciale.",
                ingredients = listOf(
                    "400g de Pasta Spaghettini",
                    "150g de Guanciale o Tocineta",
                    "4 Yemas de huevo",
                    "100g de Queso Pecorino Romano",
                    "Pimienta negra recién molida"
                )
            ),
            onStartCookingClick = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Modo Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CookingRecipeDetailScreenDarkPreview() {
    RecipeBoxTheme(darkTheme = true) {
        CookingRecipeDetailScreen(
            uiState = RecipeDetailUiState(
                isLoading = false,
                title = "Pasta Carbonara Tradicional",
                description = "Deliciosa receta italiana hecha a base de huevo, queso pecorino y guanciale.",
                ingredients = listOf(
                    "400g de Pasta Spaghettini",
                    "150g de Guanciale o Tocineta",
                    "4 Yemas de huevo",
                    "100g de Queso Pecorino Romano",
                    "Pimienta negra recién molida"
                )
            ),
            onStartCookingClick = {},
            onBackClick = {}
        )
    }
}
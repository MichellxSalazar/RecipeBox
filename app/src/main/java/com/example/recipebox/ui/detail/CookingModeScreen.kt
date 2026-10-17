package com.example.recipebox.ui.detail

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipebox.ui.theme.RecipeBoxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingModeScreen(
    uiState: RecipeDetailUiState,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onFinishCooking: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isKeepScreenOn) {
        KeepScreenOn()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modo Cocina", style = MaterialTheme.typography.titleMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val totalSteps = uiState.steps.size
            val currentStep = uiState.currentStepIndex

            // Chip de progreso del paso
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Paso ${currentStep + 1} de $totalSteps",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Tarjeta central del paso en pantalla gigante
            if (uiState.steps.isNotEmpty() && currentStep < totalSteps) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 20.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.steps[currentStep],
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Botones Inferiores de Navegación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onPreviousStep,
                    enabled = currentStep > 0,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Anterior", style = MaterialTheme.typography.titleMedium)
                }

                if (currentStep < totalSteps - 1) {
                    Button(
                        onClick = onNextStep,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Siguiente", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    Button(
                        onClick = onFinishCooking,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("Finalizar", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun KeepScreenOn() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CookingModeScreenPreview() {
    RecipeBoxTheme {
        CookingModeScreen(
            uiState = RecipeDetailUiState(
                isLoading = false,
                currentStepIndex = 1,
                steps = listOf(
                    "Paso 1: Poner a hervir abundante agua con sal en una olla grande.",
                    "Paso 2: Dorar el guanciale picado en cubos en una sartén grande a fuego medio sin agregar aceite.",
                    "Paso 3: Mezclar las yemas de huevo con el queso pecorino hasta formar una crema homogénea."
                )
            ),
            onNextStep = {},
            onPreviousStep = {},
            onFinishCooking = {}
        )
    }
}
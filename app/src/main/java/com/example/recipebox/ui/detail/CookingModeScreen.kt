package com.example.recipebox.ui.detail

import android.app.Activity
import android.content.res.Configuration
import android.view.WindowManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    val purplePrimary = Color(0xFF6750A4)
    val purpleContainer = Color(0xFFEADDFF)
    val onPurpleContainer = Color(0xFF21005D)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Modo Cocina", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
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

            // Chip Lila de progreso
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = purpleContainer,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Paso ${currentStep + 1} de $totalSteps",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = onPurpleContainer,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // Tarjeta Central con Borde Definido
            if (uiState.steps.isNotEmpty() && currentStep < totalSteps) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 20.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.steps[currentStep],
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Botones Inferiores
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
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Anterior", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                }

                if (currentStep < totalSteps - 1) {
                    Button(
                        onClick = onNextStep,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = purplePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Siguiente", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                } else {
                    Button(
                        onClick = onFinishCooking,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = purplePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Finalizar", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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

@Preview(name = "Modo Claro", showBackground = true)
@Composable
fun CookingModeScreenPreview() {
    RecipeBoxTheme(darkTheme = false) {
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

@Preview(name = "Modo Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CookingModeScreenDarkPreview() {
    RecipeBoxTheme(darkTheme = true) {
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
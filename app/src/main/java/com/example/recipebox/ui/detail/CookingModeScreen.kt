package com.example.recipebox.ui.detail

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingModeScreen(
    uiState: RecipeDetailUiState,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onFinishCooking: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mantener la pantalla encendida mientras se cocina
    if (uiState.isKeepScreenOn) {
        KeepScreenOn()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modo Cocina") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val totalSteps = uiState.steps.size
            val currentStep = uiState.currentStepIndex

            Text(
                text = "Paso ${currentStep + 1} de $totalSteps",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (uiState.steps.isNotEmpty() && currentStep < totalSteps) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.steps[currentStep],
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onPreviousStep,
                    enabled = currentStep > 0
                ) {
                    Text("Anterior")
                }

                if (currentStep < totalSteps - 1) {
                    Button(onClick = onNextStep) {
                        Text("Siguiente")
                    }
                } else {
                    Button(onClick = onFinishCooking) {
                        Text("Finalizar")
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
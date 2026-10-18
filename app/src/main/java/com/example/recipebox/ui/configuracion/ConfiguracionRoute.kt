package com.example.recipebox.ui.configuracion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recipebox.ui.screens.ConfiguracionScreen

@Composable
fun ConfiguracionRoute(
    onVolver: () -> Unit,
    viewModel: ConfiguracionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConfiguracionScreen(
        uiState = uiState,
        onPantallaEncendidaChange = viewModel::cambiarPantallaEncendida,
        onSistemaMetricoChange = viewModel::cambiarSistemaMetrico,
        onVolver = onVolver
    )
}
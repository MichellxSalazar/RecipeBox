package com.example.recipebox.ui.configuracion

import com.example.recipebox.domain.model.PreferenciasUsuario

sealed interface ConfiguracionUiState {

    data object Cargando : ConfiguracionUiState

    data class Exito(
        val preferencias: PreferenciasUsuario
    ) : ConfiguracionUiState

    data class Error(
        val mensaje: String
    ) : ConfiguracionUiState
}